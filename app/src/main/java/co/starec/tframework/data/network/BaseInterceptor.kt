package co.starec.tframework.data.network

import java.io.IOException

import javax.inject.Singleton

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
class BaseInterceptor : Interceptor {

    private var mScheme: String? = null
    private var mHost: String? = null
    private var mPort: Int = 0

    fun setBaseInterceptor(url: String, port: Int) {
        val httpUrl = HttpUrl.parse(url)
        mScheme = httpUrl.scheme()
        mHost = httpUrl.host()
        mPort = port
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        var original = chain.request()

        if (mScheme != null && mHost != null) {
            val newUrl = original.url().newBuilder()
                    .scheme(mScheme!!)
                    .host(mHost!!)
                    .port(mPort)
                    .build()
            original = original.newBuilder()
                    .url(newUrl)
                    .build()
        }

        return chain.proceed(original)
    }
}