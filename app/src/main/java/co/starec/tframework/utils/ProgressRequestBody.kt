package co.starec.tframework.utils

import android.net.Uri
import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class ProgressRequestBody(mUri: Uri, private val mListener: UploadCallbacks) : RequestBody() {
    private var mFile = File(mUri.path)
    val mName: String = mFile.name

    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Int)
        fun onError()
        fun onFinish()
    }

    override fun contentType(): MediaType {
        // i want to upload only images
        return MediaType.parse("image/*")
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    override fun writeTo(sink: BufferedSink) {

        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val inStream = FileInputStream(mFile)
        var uploaded: Long = 0

        try {
            val handler = Handler(Looper.getMainLooper())
            var read = inStream.read(buffer)
            while (read != -1) {
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                read = inStream.read(buffer)
                // update progress on UI thread
                handler.post(ProgressUpdater(uploaded, fileLength))
            }
        } finally {
            inStream.close()
        }
    }

    private inner class ProgressUpdater(private val mUploaded: Long, private val mTotal: Long) : Runnable {

        override fun run() {
            mListener.onProgressUpdate((100 * mUploaded / mTotal).toInt())
        }
    }

    companion object {

        private val DEFAULT_BUFFER_SIZE = 2048
    }
}