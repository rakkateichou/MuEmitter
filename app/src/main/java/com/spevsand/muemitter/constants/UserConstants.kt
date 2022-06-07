package com.spevsand.muemitter.constants

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.spevsand.muemitter.model.User

object UserConstants {
    val appFirebaseAuth = Firebase.auth
    val appFirebaseUser = appFirebaseAuth.currentUser
    lateinit var user: User

    const val USER_SHARED_PREF = "user_shared_pref"
}