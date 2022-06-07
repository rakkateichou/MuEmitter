package com.spevsand.muemitter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.spevsand.muemitter.viewmodel.livedata.CredentialsLiveData


class LoginRegisterViewModel(application: Application) : AndroidViewModel(application) {
    val mCredential = CredentialsLiveData(application.applicationContext)
}