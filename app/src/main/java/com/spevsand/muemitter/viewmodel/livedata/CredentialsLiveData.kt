package com.spevsand.muemitter.viewmodel.livedata

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.gms.auth.api.credentials.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class CredentialsLiveData(context: Context) : LiveData<Task<CredentialRequestResponse>>() {
    private val credentialsClient: CredentialsClient = Credentials.getClient(context)
    private val mCredentialRequest: CredentialRequest = CredentialRequest.Builder()
        .setPasswordLoginSupported(true)
        .build()
    private val listener: MyOnCompleteListener = MyOnCompleteListener()

    override fun onActive() {
        super.onActive()
        credentialsClient.request(mCredentialRequest).addOnCompleteListener(listener)
    }

    /*fun observe(owner: LifecycleOwner, credentialsClient: CredentialsClient, observer: Observer<in Credential>) {
        this.credentialsClient = credentialsClient
        super.observe(owner, observer)
    }*/

    private inner class MyOnCompleteListener : OnCompleteListener<CredentialRequestResponse> {
        override fun onComplete(task: Task<CredentialRequestResponse>) {
            task.let { value = it }
        }
    }

}