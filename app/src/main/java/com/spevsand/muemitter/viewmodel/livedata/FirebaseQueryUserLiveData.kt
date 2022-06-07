package com.spevsand.muemitter.viewmodel.livedata

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.spevsand.muemitter.model.User

class FirebaseQueryUserLiveData(val ref: DatabaseReference) : LiveData<User>() {
    private val listener: MyValueEventListener = MyValueEventListener()
    private val handler = Handler()
    private var listenerRemovePending = false
    private val removeListenerRunnable = Runnable {
        ref.removeEventListener(listener)
        listenerRemovePending = false
    }

    override fun onActive() {
        if (listenerRemovePending) handler.removeCallbacks(removeListenerRunnable)
        else ref.addValueEventListener(listener)
        listenerRemovePending = false
    }

    override fun onInactive() {
        handler.postDelayed(removeListenerRunnable, 2000L)
        listenerRemovePending = true
    }

    private inner class MyValueEventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            value = dataSnapshot.getValue(User::class.java)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e(
                LOG_TAG,
                "Can't listen to ref $ref", databaseError.toException()
            )
        }
    }

    companion object {
        private const val LOG_TAG = "FBQueryLiveData"
    }
}