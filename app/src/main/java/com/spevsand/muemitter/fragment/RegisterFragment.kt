package com.spevsand.muemitter.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.spevsand.muemitter.R
import com.spevsand.muemitter.activity.LoginRegisterActivity
import com.spevsand.muemitter.activity.MainActivity
import com.spevsand.muemitter.constants.Constants.appRTDBReference
import com.spevsand.muemitter.constants.UserConstants.appFirebaseAuth

class RegisterFragment : Fragment() {

    private lateinit var mLoginFragment: LoginFragment

    private lateinit var mRegisterEmail: EditText
    private lateinit var mRegisterPassword: EditText
    private lateinit var mRegisterButton: Button
    private lateinit var mRegisterNickname: EditText
    private lateinit var mToLogin: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_register, container, false)

        mLoginFragment = LoginFragment()

        mRegisterEmail = v.findViewById(R.id.register_email_edittext)
        mRegisterPassword = v.findViewById(R.id.register_password_edittext)
        mRegisterButton = v.findViewById(R.id.register_button)
        mRegisterNickname = v.findViewById(R.id.register_nickname_edittext)
        mToLogin = v.findViewById(R.id.register_to_login_textview)

        mRegisterButton.setOnClickListener {
            if (mRegisterEmail.text.toString().trim() != "" && mRegisterPassword.text.toString()
                    .trim() != "" && mRegisterNickname.text.toString().trim() != ""
            ) {
                appFirebaseAuth.createUserWithEmailAndPassword(
                    mRegisterEmail.text.trim().toString(),
                    mRegisterPassword.text.trim().toString()
                ).addOnSuccessListener {
                    appRTDBReference.child("users").child(it.user?.uid!!)
                        .setValue(mRegisterNickname.text.toString().trim())
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

        mToLogin.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.login_register_parent_layout, mLoginFragment)?.commit()
        }

        return v
    }
}