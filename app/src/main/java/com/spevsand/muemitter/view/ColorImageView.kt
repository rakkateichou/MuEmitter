package com.spevsand.muemitter.view

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import java.lang.NullPointerException

class ColorImageView(context: Context, attributeSet: AttributeSet) : androidx.appcompat.widget.AppCompatImageView(context, attributeSet), Colorable{
    override fun color(color: Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //child.background.colorFilter = BlendModeColorFilter(it, BlendMode.MULTIPLY)
        } else {
            try {
                background.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
            } catch (e: NullPointerException){
                try{
                    this.drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
                }
                catch (e: NullPointerException){}
            }
        }
    }
}