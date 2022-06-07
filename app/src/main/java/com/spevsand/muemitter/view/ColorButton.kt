package com.spevsand.muemitter.view

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet

class ColorButton(context: Context, attributeSet: AttributeSet) : androidx.appcompat.widget.AppCompatButton(context, attributeSet), Colorable {
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