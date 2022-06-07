package com.spevsand.muemitter.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.larswerkman.holocolorpicker.ColorPicker
import com.larswerkman.holocolorpicker.SVBar
import com.spevsand.muemitter.databinding.ActivityChangeThemeBinding
import com.spevsand.muemitter.constants.Constants.paintWithSavedColor
import com.spevsand.muemitter.constants.Constants.saveColor

class ChangeThemeActivity : AppCompatActivity() {

    private lateinit var mColorPicker: ColorPicker
    private lateinit var mSVBar: SVBar
    private lateinit var binding: ActivityChangeThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChangeThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        mColorPicker = binding.activityChangeThemeColorPicker
        mSVBar = binding.activityChangeThemeSvBar
        mColorPicker.addSVBar(mSVBar)
        mColorPicker.showOldCenterColor = false
        mColorPicker.color =
            paintWithSavedColor(binding.activityChangeThemeLayout)

        mColorPicker.setOnColorChangedListener {
            saveColor(this, it)
            paintWithSavedColor(binding.activityChangeThemeLayout)
        }

    }
}