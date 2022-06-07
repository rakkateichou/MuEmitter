package com.spevsand.muemitter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.spevsand.muemitter.model.AudioData
import com.spevsand.muemitter.model.Message
import com.spevsand.muemitter.constants.Constants.appFirestoreReference
import com.spevsand.muemitter.constants.Constants.appRTDBReference
import com.spevsand.muemitter.constants.Constants.notToken
import com.spevsand.muemitter.constants.UserConstants.appFirebaseUser
import com.spevsand.muemitter.constants.UserConstants.user
import com.spevsand.muemitter.viewmodel.livedata.FirebaseQueryArrayLiveData
import com.spevsand.muemitter.viewmodel.livedata.FirebaseQueryUserLiveData
import com.spevsand.muemitter.viewmodel.livedata.FirestoreQueryArrayLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val userDatabaseReference = appRTDBReference.child(FS_USER_USER_PATH)
    val userSnapshotLiveData = FirebaseQueryUserLiveData(userDatabaseReference)

    private val playlistDatabaseReference = appRTDBReference.child(FB_PLAYLIST_PATH)
    private val playlistLiveData = FirebaseQueryArrayLiveData(playlistDatabaseReference)

    private val messagesFirestoreReference = appFirestoreReference.collection(FS_MESSAGES_PATH)
    private val messagesLiveData = FirestoreQueryArrayLiveData(messagesFirestoreReference)

    val mediatorPlaylistLiveData = MediatorLiveData<ArrayList<AudioData>>()
    val mediatorMessagesLiveData = MediatorLiveData<ArrayList<Message>>()

    init {
        mediatorPlaylistLiveData.addSource(playlistLiveData) { it ->
            it?.let {
                GlobalScope.launch {
                    mediatorPlaylistLiveData.postValue(it.map {
                        it?.getValue(AudioData::class.java).apply { this?.id = it?.key!! }
                            ?: AudioData()
                    }.toCollection(ArrayList()))
                }
            }
                ?: run { mediatorPlaylistLiveData.value = null }
        }
        mediatorMessagesLiveData.addSource(messagesLiveData) { it ->
            it?.let { arrayList ->
                GlobalScope.launch {
                    mediatorMessagesLiveData.postValue(arrayList.map {
                        it.toObject(Message::class.java) ?: Message()
                    }.toCollection(ArrayList()))
                }
            }
                ?: run { mediatorMessagesLiveData.value = null }
        }
    }

    fun passMessageText(text: String) {
        appFirestoreReference.collection("messages").add(
            Message(
                user.username,
                (if (text != "NOT_TOKEN") text else notToken!!),
                System.currentTimeMillis(),
                appFirebaseUser!!.uid
            )
        )
    }

    companion object {
        const val FB_PLAYLIST_PATH = "/playlist"
        const val FS_MESSAGES_PATH = "messages"
        val FS_USER_USER_PATH = "users/${appFirebaseUser!!.uid}"
    }
}