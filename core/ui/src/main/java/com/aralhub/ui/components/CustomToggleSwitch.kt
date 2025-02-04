package com.aralhub.ui.components


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.aralhub.ui.R

class CustomToggleSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var isChecked = false
    private var switchPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var onColor = ContextCompat.getColor(context, R.color.color_interactive_accent)
    private var offColor = ContextCompat.getColor(context, R.color.color_content_tertiary)
    private var thumbColor = Color.WHITE

    private var switchWidth = 0
    private var switchHeight = 0
    private var thumbRadius = 0f
    private var thumbX = 0f

    init {
        setOnClickListener {
            toggle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = (width * 0.5).toInt()
        setMeasuredDimension(width, height)

        switchWidth = width
        switchHeight = height
        thumbRadius = height * 0.4f
        thumbX = if (isChecked) switchWidth - thumbRadius * 2 - 10 else 10f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw switch background
        switchPaint.color = if (isChecked) onColor else offColor
        val rect = RectF(0f, 0f, switchWidth.toFloat(), switchHeight.toFloat())
        canvas.drawRoundRect(rect, switchHeight / 2f, switchHeight / 2f, switchPaint)

        // Draw thumb
        thumbPaint.color = thumbColor
        canvas.drawCircle(thumbX + thumbRadius, switchHeight / 2f, thumbRadius, thumbPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                toggle()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun toggle() {
        isChecked = !isChecked
        animateThumb()
        invalidate()
    }

    private fun animateThumb() {
        val targetX = if (isChecked) switchWidth - thumbRadius * 2 - 10 else 10f
        thumbX = targetX
    }

    fun isChecked(): Boolean = isChecked
    fun setChecked(checked: Boolean) {
        isChecked = checked
        thumbX = if (checked) switchWidth - thumbRadius * 2 - 10 else 10f
        invalidate()
    }
}
