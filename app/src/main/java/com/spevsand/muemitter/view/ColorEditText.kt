package com.spevsand.muemitter.view

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet

class ColorEditText(context: Context, attributeSet: AttributeSet) : androidx.appcompat.widget.AppCompatEditText(context, attributeSet), Colorable {
    override fun color(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //child.background.colorFilter = BlendModeColorFilter(it, BlendMode.MULTIPLY)
        } else {
            background.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        }
    }
}