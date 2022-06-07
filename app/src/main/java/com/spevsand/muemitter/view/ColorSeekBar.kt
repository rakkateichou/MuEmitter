package com.spevsand.muemitter.view

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet

class ColorSeekBar(context: Context,attributeSet: AttributeSet) : androidx.appcompat.widget.AppCompatSeekBar(context, attributeSet), Colorable {
    override fun color(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //child.background.colorFilter = BlendModeColorFilter(it, BlendMode.MULTIPLY)
        } else {
            progressDrawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
            thumb.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }
}