package com.spevsand.muemitter.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.spevsand.muemitter.R
import com.spevsand.muemitter.constants.UserConstants.appFirebaseUser

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            val intent =
                if (appFirebaseUser != null) Intent(this, MainActivity::class.java)
                else Intent(this, LoginRegisterActivity::class.java)
            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
            finish()
            overridePendingTransition(0, 0)
        }, 1050)
    }
}