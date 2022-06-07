package com.spevsand.muemitter.viewmodel.livedata

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*

class FirestoreQueryArrayLiveData(private val collectionReference: CollectionReference) :
    LiveData<ArrayList<DocumentSnapshot>>() {
    private lateinit var listenerRegistration: ListenerRegistration
    private val handler = Handler()
    private var listenerRemovePending = false
    private val removeListenerRunnable = Runnable {
        listenerRegistration.remove()
        listenerRemovePending = false
    }

    override fun onActive() {
        if (listenerRemovePending) handler.removeCallbacks(removeListenerRunnable)
        else listenerRegistration = collectionReference.orderBy("unixTime", Query.Direction.ASCENDING).limit(70).addSnapshotListener(MyEventListener())
        listenerRemovePending = false
    }

    override fun onInactive() {
        handler.postDelayed(removeListenerRunnable, 2000L)
        listenerRemovePending = true
    }

    private inner class MyEventListener : EventListener<QuerySnapshot> {
        override fun onEvent(eventValue: QuerySnapshot?, error: FirebaseFirestoreException?) {
            if (error != null) {
                Log.e(
                    LOG_TAG,
                    "$error"
                )
            } else {
                value = eventValue?.documents?.toCollection(ArrayList())
            }
        }

    }

    companion object {
        private const val LOG_TAG = "FSQueryArrayLiveData"
    }
}