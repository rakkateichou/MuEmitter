package com.spevsand.muemitter.view

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class ColorConstraintLayout(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet), Colorable {
    override fun color(color: Int) {
        //Log.e("aaaAAAAAAAAAAaaa", this.toString())
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                background.colorFilter = BlendModeColorFilter(color, BlendMode.MULTIPLY)
            } else {
                this.background.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
            }
        } catch (e: NullPointerException){}
    }

}