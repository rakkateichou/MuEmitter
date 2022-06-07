package com.spevsand.muemitter.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spevsand.muemitter.databinding.ActivityEditProfileBinding
import com.spevsand.muemitter.constants.Constants.appRTDBReference
import com.spevsand.muemitter.constants.Constants.paintWithSavedColor
import com.spevsand.muemitter.constants.UserConstants.appFirebaseUser
import com.spevsand.muemitter.constants.UserConstants.user

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        paintWithSavedColor(binding.activityEditProfileMain)
        binding.activityEditProfileEditNickname.setText(user.username)
        binding.activityEditProfileSubmitNicknameButton.setOnClickListener {
            if (binding.activityEditProfileEditNickname.text?.isNotBlank()!!) {
                appRTDBReference.child("users").child(appFirebaseUser!!.uid).updateChildren(
                    mapOf<String, String>(
                        Pair(
                            "username",
                            binding.activityEditProfileEditNickname.text!!.trim().toString()
                        )
                    )
                )
            }
        }
    }
}