package com.spevsand.muemitter.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.spevsand.muemitter.R
import com.spevsand.muemitter.activity.LoginRegisterActivity
import com.spevsand.muemitter.activity.LoginRegisterActivity.Companion.CREDENTIALS_SAVE_REQUEST
import com.spevsand.muemitter.activity.MainActivity
import com.spevsand.muemitter.constants.UserConstants.appFirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var mRegisterFragment: RegisterFragment

    private lateinit var mLoginEmail: EditText
    private lateinit var mLoginPassword: EditText
    private lateinit var mLoginButton: Button
    private lateinit var mToRegister: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_login, container, false)

        mRegisterFragment = RegisterFragment()

        mLoginEmail = v.findViewById(R.id.login_email_edittext)
        mLoginPassword = v.findViewById(R.id.login_password_edittext)
        mLoginButton = v.findViewById(R.id.login_button)
        mToRegister = v.findViewById(R.id.login_to_register_textview)

        mLoginButton.setOnClickListener {
            if (mLoginEmail.text.trim().toString() != "" && mLoginPassword.text.trim()
                    .toString() != ""
            ) {
                appFirebaseAuth.signInWithEmailAndPassword(
                    mLoginEmail.text.trim().toString(),
                    mLoginPassword.text.trim().toString()
                ).addOnSuccessListener {
                    val credential: Credential =
                        Credential.Builder(mLoginEmail.text.trim().toString())
                            .setPassword(mLoginPassword.text.trim().toString())
                            .build()
                    (activity as LoginRegisterActivity).mCredentialsClient.save(credential)
                        .addOnCompleteListener(MyOnCompleteListener())
                    val intent = Intent(this.context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }.addOnFailureListener {
                    (activity as LoginRegisterActivity).openSnackbar(it.localizedMessage!!)
                }
            } else {
                (activity as LoginRegisterActivity).openSnackbar(getString(R.string.fill_in_all_the_fields))
            }
        }

        mToRegister.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.login_register_parent_layout, mRegisterFragment)?.commit()
        }

        return v
    }

    inner class MyOnCompleteListener : OnCompleteListener<Void> {
        override fun onComplete(task: Task<Void>) {
            if (task.isSuccessful) {
                Toast.makeText(activity, "Credentials saved", Toast.LENGTH_SHORT).show()
                return
            }

            task.exception?.let {
                if (it is ResolvableApiException) {
                    // Try to resolve the save request. This will prompt the user if
                    // the credential is new.
                    try {
                        it.startResolutionForResult(
                            this@LoginFragment.activity,
                            CREDENTIALS_SAVE_REQUEST
                        )
                    } catch (exception: SendIntentException) {
                        // Could not resolve the request
                        Toast.makeText(activity, getString(R.string.credentials_save_failed), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Request has no resolution
                    Toast.makeText(activity, getString(R.string.credentials_save_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREDENTIALS_SAVE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this@LoginFragment.activity, getString(R.string.credentials_saved), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}