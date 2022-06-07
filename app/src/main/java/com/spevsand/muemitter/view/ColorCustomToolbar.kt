package com.spevsand.muemitter.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import com.spevsand.muemitter.R
import com.spevsand.muemitter.databinding.ToolbarCustomSettingsBinding

class ColorCustomToolbar(context: Context, attributeSet: AttributeSet) :
    Toolbar(context, attributeSet), Colorable {

    private var binding: ToolbarCustomSettingsBinding =
        ToolbarCustomSettingsBinding.inflate(LayoutInflater.from(context))

    init {
        inflate(context, R.layout.toolbar_custom_settings, this)
        binding.toolbarSettingsBack.setOnClickListener {
            (context as Activity).finish()
        }
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.ColorCustomToolbar,
            0, 0
        )
            .apply {
                try {
                    binding.toolbarSettingsName.text =
                        this.getString(R.styleable.ColorCustomToolbar_toolbarTitle)
                } finally {
                    recycle()
                }
            }
        this.setContentInsetsAbsolute(0, 0)
    }

    override fun color(color: Int) {
        binding.toolbarSettingsColorConstraintLayout.color(color)
        binding.toolbarSettingsName.color(color)
        binding.toolbarSettingsBack.color(color)
    }

}