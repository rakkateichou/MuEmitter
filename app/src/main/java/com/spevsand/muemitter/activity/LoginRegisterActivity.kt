package com.spevsand.muemitter.activity

import android.content.Intent
import android.content.IntentSender
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.material.snackbar.Snackbar
import com.spevsand.muemitter.R
import com.spevsand.muemitter.fragment.LoginFragment
import com.spevsand.muemitter.constants.UserConstants
import com.spevsand.muemitter.viewmodel.LoginRegisterViewModel

class LoginRegisterActivity : AppCompatActivity(R.layout.activity_login_register) {

    private lateinit var mErrorSnackbar: Snackbar

    private lateinit var mLoginFragment: LoginFragment
    lateinit var mCredentialsClient: CredentialsClient
    private val viewModel: LoginRegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCredentialsClient = Credentials.getClient(this)
        //Log.e("CRASH_IT", mCredentialsClient.toString())

        mErrorSnackbar = Snackbar.make(
            findViewById(R.id.login_register_parent_layout)!!,
            "Error",
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(
            Color.RED
        ).setTextColor(Color.BLACK)
        val font = ResourcesCompat.getFont(
            applicationContext!!,
            R.font.thintel
        )
        mErrorSnackbar.view.findViewById<TextView>(R.id.snackbar_text)
            .apply {
                this.typeface = font
                this.textSize = 33f
                this.maxLines = 5

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.textAlignment = View.TEXT_ALIGNMENT_CENTER
                } else {
                    this.gravity = Gravity.CENTER_HORIZONTAL
                }
            }

        viewModel.mCredential.observe(this) {
            if (it.isSuccessful) {
                onCredentialRetrieved(it.result?.credential!!)
            } else {
                val e: Exception? = it.exception
                if (e is ResolvableApiException) {
                    try {
                        e.startResolutionForResult(this, CREDENTIALS_GET_REQUEST)
                    } catch (e: IntentSender.SendIntentException) {
                        openSnackbar(getString(R.string.failed_to_send_resolution))
                    }
                } else {
                    openSnackbar(getString(R.string.unsuccessful_credential_request))
                }
            }
        }

        mLoginFragment = LoginFragment()

        supportFragmentManager.beginTransaction().add(
            R.id.login_register_parent_layout,
            mLoginFragment
        ).commit()

    }

    fun openSnackbar(text: String) {
        mErrorSnackbar.setText(text)
        mErrorSnackbar.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREDENTIALS_GET_REQUEST) {
            if (resultCode == RESULT_OK) {
                val credential: Credential = data!!.getParcelableExtra(Credential.EXTRA_KEY)!!
                onCredentialRetrieved(credential)
            } else {
                openSnackbar(getString(R.string.credential_read_failed))
            }
        }
    }

    private fun onCredentialRetrieved(credential: Credential) {
        UserConstants.appFirebaseAuth.signInWithEmailAndPassword(
            credential.id,
            credential.password ?: ""
        )
            .addOnCompleteListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                openSnackbar(getString(R.string.failed_to_sign_in_with_google_smartlock))
            }
    }

    companion object {
        const val CREDENTIALS_SAVE_REQUEST = 0
        const val CREDENTIALS_GET_REQUEST = 0
    }

}