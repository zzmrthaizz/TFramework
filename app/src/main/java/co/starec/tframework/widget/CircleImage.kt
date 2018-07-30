package co.starec.runningapp.runther.framework.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView

class CircleImage : ImageView {

    private var borderWidth = 1
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0
    private var image: Bitmap? = null
    private var paint: Paint? = null
    private var paintBorder: Paint? = null
    private var shader: BitmapShader? = null

    constructor(context: Context) : super(context) {
        setup()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setup()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setup()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        //load the bitmap
        loadBitmap()

        // init shader
        if (image != null) {
            shader = BitmapShader(Bitmap.createScaledBitmap(image, canvas.getWidth(), canvas.getHeight(), false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint!!.setShader(shader)
            val circleCenter: Float = (viewWidth / 2).toFloat()

            // circleCenter is the x or y of the view's center
            // radius is the radius in pixels of the cirle to be drawn
            // paint contains the shader that will texture the shape
            canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter + borderWidth, paintBorder)
            canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter, paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec, widthMeasureSpec)

        viewWidth = width - borderWidth * 2
        viewHeight = height - borderWidth * 2

        setMeasuredDimension(width, height)
    }

    private fun measureWidth(measureSpec: Int): Int {
        val result : Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        result = if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            specSize
        } else {
            // Measure the text
            viewWidth

        }

        return result
    }

    private fun setup() {
        // init paint
        paint = Paint()
        paint!!.setAntiAlias(true)

        paintBorder = Paint()
        setBorderColor(getResources().getColor(android.R.color.white))
        paintBorder!!.setAntiAlias(true)
    }

    fun setBorderWidth(borderWidth: Int) {
        this.borderWidth = borderWidth
        this.invalidate()
    }

    fun setBorderColor(borderColor: Int) {
        if (paintBorder != null)
            paintBorder!!.color = borderColor

        this.invalidate()
    }

    private fun loadBitmap() {
        if (drawable != null) {
            val bitmapDrawable = this.drawable as BitmapDrawable
            image = bitmapDrawable.bitmap
        }
    }

    private fun measureHeight(measureSpecHeight: Int, measureSpecWidth: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpecHeight)
        val specSize = MeasureSpec.getSize(measureSpecHeight)

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = viewHeight
        }
        return result
    }
}