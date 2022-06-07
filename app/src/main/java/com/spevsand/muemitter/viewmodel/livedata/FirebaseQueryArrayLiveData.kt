package com.spevsand.muemitter.viewmodel.livedata

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*

class FirebaseQueryArrayLiveData : LiveData<ArrayList<DataSnapshot?>> {
    private var query: Query? = null
    private val listener: MyValueEventListener = MyValueEventListener()
    private val handler = Handler()
    private var listenerRemovePending = false
    private val removeListenerRunnable = Runnable{
        query?.removeEventListener(listener)
        listenerRemovePending = false
    }

    constructor(query: Query) {
        this.query = query
    }

    constructor(ref: DatabaseReference) {
        query = ref
    }

    override fun onActive() {
        if(listenerRemovePending) handler.removeCallbacks(removeListenerRunnable)
        else query?.addValueEventListener(listener)
        listenerRemovePending = false
    }

    override fun onInactive() {
        handler.postDelayed(removeListenerRunnable, 2000L)
        listenerRemovePending = true
    }

    private inner class MyValueEventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            value = dataSnapshot.children.toCollection(ArrayList())
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e(
                LOG_TAG,
                "Can't listen to query $query", databaseError.toException()
            )
        }
    }

    companion object {
        private const val LOG_TAG = "FBQueryArrayLiveData"
    }
}