package com.spevsand.muemitter.view

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView


class ColorVerticalTextView(context: Context?, attrs: AttributeSet?) : AppCompatTextView(
    context!!,
    attrs
), Colorable{

    private var topDown = false
    private var textPaint = paint

    init {
        val gravity = gravity
        topDown = if (Gravity.isVertical(gravity)
            && gravity and Gravity.VERTICAL_GRAVITY_MASK == Gravity.BOTTOM
        ) {
            setGravity(
                gravity and Gravity.HORIZONTAL_GRAVITY_MASK
                        or Gravity.TOP
            )
            false
        } else {
            true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(canvas: Canvas) {
        textPaint.color = currentTextColor
        textPaint.drawableState = drawableState
        canvas.save()
        if (topDown) {
            canvas.translate(width.toFloat(), 0f)
            canvas.rotate(90f)
        } else {
            canvas.translate(0f, height.toFloat())
            canvas.rotate(-90f)
        }
        canvas.translate(compoundPaddingLeft.toFloat(), extendedPaddingTop.toFloat())
        layout.draw(canvas)
        canvas.restore()
    }

    override fun color(color: Int) {
        setTextColor(color)
        if (background != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                background.colorFilter = BlendModeColorFilter(color, BlendMode.MULTIPLY)
            } else {
                this.background.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
            }
        }
    }
}