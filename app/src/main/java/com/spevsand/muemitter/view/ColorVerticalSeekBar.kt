package com.spevsand.muemitter.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat
import com.spevsand.muemitter.R

/**
 * Vertical Progress Bar For Android Apps
 */
class ColorVerticalSeekBar : AppCompatSeekBar, Colorable {

    constructor(context: Context) : super(context) {
        initView(context)
    }
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int) : super(context, attrs, defStyle) {
        initView(context)
    }
    constructor(
        context: Context,
        attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    private fun initView(context: Context) {
        // Set Custom Progress Bar Drawable
        progressDrawable = ContextCompat.getDrawable(context, R.drawable.vertical_progress_selector)
        // Set Custom Thumb
        thumb = ContextCompat.getDrawable(context, R.drawable.vertical_progress_thumb)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int){
        super.onSizeChanged(h, w, oldh, oldw)
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(c: Canvas) {
        c.rotate(-90f)
        c.translate(-height.toFloat(), 0f)
        super.onDraw(c)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                //parent.requestDisallowInterceptTouchEvent(true)
                progress = max - (max * event.y / height).toInt()
                onSizeChanged(width, height, 0, 0)
            }
        }
        return true
    }

    override fun color(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //child.background.colorFilter = BlendModeColorFilter(it, BlendMode.MULTIPLY)
        } else {
            progressDrawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
            thumb.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }
}