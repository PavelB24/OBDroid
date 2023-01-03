package ru.barinov.obdroid.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ru.barinov.obdroid.R
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sin


class SpeedometerView(
    context: Context,
    attributes: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(
    context,
    attributes,
    defStyleAttr,
    defStyleRes
) {

    constructor(context: Context, attributes: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attributes,
        0,
        0
    )

    constructor(context: Context, attributes: AttributeSet?) : this(context, attributes, 0)
    constructor(context: Context) : this(context, null)

    private val idlAspect = 2f / 1f
    private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = 0xff0000
        it.style = Paint.Style.FILL
        it.strokeWidth = 0.005f
        it.alpha = 255
    }

    private lateinit var backgroundPaint: Paint
    private lateinit var scalePaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var pointerPaint: Paint

    private val partScale = 0.91f
    private val markRange = 20
    private var maxSpeed: Int? = null
    private var step: Double = 0.00
    private var currentSpeed = 1
    private val visibilityCoef = 0.8f
    private val shouldShowWarning = false

    init {
        attributes?.let {
            initAttributes(context, attributes, defStyleAttr, defStyleRes)
        }
    }

    private fun initAttributes(
        context: Context,
        attributes: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val tArray = context.obtainStyledAttributes(attributes, R.styleable.SpeedometerView)

        val backgroundColor = tArray.getColor(R.styleable.SpeedometerView_backgroundColor, 0x010101)
        val backgroundAlpha = tArray.getInt(R.styleable.SpeedometerView_backgroundColor, 80)
        backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.color = backgroundColor
            it.style = Paint.Style.FILL
            it.alpha = backgroundAlpha
        }

        val pointerColor = tArray.getColor(R.styleable.SpeedometerView_pointerColor, 0xCC0000)
        val pointerAlpha = tArray.getInt(R.styleable.SpeedometerView_pointerAlpha, 255)
        pointerPaint = Paint().also {
            it.color = pointerColor
            it.strokeWidth = 0.02f
            it.alpha = pointerAlpha
        }

        val speedTextColor = tArray.getColor(R.styleable.SpeedometerView_speedTextColor, 0x000000)
        val speedTextAlpha = tArray.getColor(R.styleable.SpeedometerView_speedTextAlpha, 255)
        textPaint = Paint().also {
            it.color = speedTextColor
            it.textSize = 40f;
            it.style = Paint.Style.FILL
            it.alpha = speedTextAlpha
            it.strokeWidth = 1f
        }

        val scaleColor = tArray.getColor(R.styleable.SpeedometerView_scaleColor, 0xff0000)
        val scaleAlpha = tArray.getInt(R.styleable.SpeedometerView_scaleAlpha, 255)
        scalePaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.color = scaleColor
            it.style = Paint.Style.STROKE
            it.strokeWidth = 0.005f
            it.alpha = scaleAlpha
        }
        val suggestedSpeed = tArray.getInt(R.styleable.SpeedometerView_maxSpeed, 180)
        maxSpeed = when (suggestedSpeed) {
            in 80..240 -> suggestedSpeed
            in Int.MIN_VALUE..80 -> 80
            else -> 240
        }

        maxSpeed?.let {
            step = Math.PI / it * visibilityCoef
        }
        tArray.recycle()
    }

    fun setSpeed(speed: Int) {
        maxSpeed?.let { max ->
            if (currentSpeed != speed) {
                currentSpeed = when (speed) {
                    in Int.MIN_VALUE..0 -> 0
                    in 0..max -> speed
                    else -> max
                }
                invalidate()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val minWidth = suggestedMinimumWidth + paddingLeft + paddingEnd
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom


        var width = MeasureSpec.getSize(widthMeasureSpec) + paddingEnd + paddingStart
        var height = MeasureSpec.getSize(heightMeasureSpec) + paddingTop + paddingBottom

        val aspect: Float = width / height.toFloat()
        if (aspect > idlAspect) {
            width = (idlAspect * height).roundToInt() + paddingEnd + paddingStart
        } else if (aspect < idlAspect) {
            height = (width / idlAspect).roundToInt() + paddingTop + paddingBottom
        }
        val resolvedWidth = max(minWidth, width)
        val resolvedHeight = max(minHeight, height)
        setMeasuredDimension(
            resolveSize(resolvedWidth, widthMeasureSpec),
            resolveSize(resolvedHeight, heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val safeW = w - paddingStart - paddingEnd
        val safeH = h - paddingTop - paddingBottom
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            val aspect = width / height
            var width: Float = width.toFloat()
            var height: Float = height.toFloat()
            if (aspect > idlAspect) {
                width = idlAspect * height
            } else if (aspect < idlAspect) {
                height = width / idlAspect
            }
            save()

            translate(width / 2, height);
            scale(.5f * width, -1f * height);
            drawCircle(0f, 0f, 1f, backgroundPaint)
            drawCircle(0f, 0f, 0.8f, backgroundPaint)
            maxSpeed?.let { max ->
                for (i in 0..max) {
                    if (i % 5 == 0) {
                        val x1 = cos(Math.PI - step * i).toFloat()
                        val y1 = sin(Math.PI - step * i).toFloat()
                        var x2 = x1 * partScale
                        var y2 = y1 * partScale
                        if (i % 20 == 0) {
                            x2 *= 0.9f
                            y2 *= 0.9f
                        }
                        drawLine(x1, y1, x2, y2, scalePaint)
                    }
                }
                if (shouldShowWarning) {
                    drawCircle(0.5f, 0.055f, 0.06f, pointPaint)
                }
                val scale = 0.9f
                val longScale = 0.9f
                val textPadding = 0.85f
                val factor: Float = height * scale * longScale * textPadding

                restore()
                save()

                translate(width / 2, 0f)
                for (i in 0..max step markRange) {
                    val x1 = cos(Math.PI - step * i).toFloat() * factor
                    val y1 = sin(Math.PI - step * i).toFloat() * factor
                    val text = i.toString()
                    val textLen = textPaint.measureText(text).roundToInt()
                    drawText(text, x1 - textLen / 2, height - y1, textPaint)
                }

                restore()
                save()

                translate(width / 2, height)
                scale(.5f * width, -1f * height)
                rotate(90 - 180f * (currentSpeed / max.toFloat() * visibilityCoef))
                drawLine(0.01f, 0f, 0f, 0.98f, pointerPaint);
                drawLine(-0.01f, 0f, 0f, 0.98f, pointerPaint);
                drawCircle(0f, 0f, 0.05f, pointPaint)
                save()
            }
        }

    }


}