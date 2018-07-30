package co.starec.tframework.utils.animation

import android.graphics.Camera
import android.view.animation.AccelerateInterpolator
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import co.starec.tframework.listener.IAnimationCompleteListener


class Flip3dAnimation(view: View, iAnimationCompleteListener: IAnimationCompleteListener, duration: Long,
                      fromDegree: Float, toDegree: Float) : Animation() {
    private val mFromDegrees: Float = fromDegree
    private val mToDegrees: Float = toDegree
    private val mCenterX: Float = view.width / 2.0f
    private val mCenterY: Float = 0f
    private var mCamera: Camera? = null
    private val mIAnimationCompleteListener : IAnimationCompleteListener = iAnimationCompleteListener

    init {
        this.duration = duration
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int,
                            parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        mCamera = Camera()
    }

    fun applyPropertiesInRotation() {
        this.fillAfter = true
        this.interpolator = AccelerateInterpolator()
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val fromDegrees = mFromDegrees
        val degrees = fromDegrees + (mToDegrees - fromDegrees) * interpolatedTime

        val centerX = mCenterX
        val centerY = mCenterY
        val camera = mCamera

        val matrix = t.matrix

        camera!!.save()

        Log.e("Degree", "" + degrees)
        Log.e("centerX", "" + centerX)
        Log.e("centerY", "" + centerY)

        camera.rotateX(degrees)

        camera.getMatrix(matrix)
        camera.restore()

        matrix.preTranslate(-centerX, -centerY)
        matrix.postTranslate(centerX, centerY)

    }
}