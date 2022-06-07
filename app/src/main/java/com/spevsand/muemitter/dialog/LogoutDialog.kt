package com.spevsand.muemitter.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.spevsand.muemitter.R
import com.spevsand.muemitter.activity.LoginRegisterActivity
import com.spevsand.muemitter.constants.Constants.paintWithSavedColor
import com.spevsand.muemitter.constants.UserConstants.appFirebaseAuth

class LogoutDialog(private val activity: Activity) : Dialog(activity),
    View.OnClickListener {
    private var yes: Button? = null
    private var no: Button? = null
    private val auth: FirebaseAuth = appFirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_logout)
        yes = findViewById(R.id.dialog_logout_yes)
        no = findViewById(R.id.dialog_logout_no)
        yes?.setOnClickListener(this)
        no?.setOnClickListener(this)
        paintWithSavedColor(findViewById(R.id.dialog_logout_main))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.dialog_logout_yes -> {
                auth.signOut()
                activity.startActivity(Intent(activity, LoginRegisterActivity::class.java))
                activity.finish()
            }
            R.id.dialog_logout_no -> dismiss()
            else -> {
            }
        }
        dismiss()
    }
}