package com.spevsand.muemitter.notifications

import android.content.SharedPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AppMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        messagingToken = token
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }

    companion object {
        private const val IS_RECEIVING_NOTIFICATIONS = "is_receiving_notifications"
        private const val FIREBASE_MESSAGING_TOKEN = "firebase_messaging_token"
        const val SHARED_PREF_NOTIFICATIONS = "shared_pref_notifications"

        var sharedPreferences: SharedPreferences? = null

        var isReceivingNotifications: Boolean
            get() {
                return sharedPreferences?.getBoolean(IS_RECEIVING_NOTIFICATIONS, true)!!
            }
            set(value) {
                sharedPreferences?.edit()?.putBoolean(IS_RECEIVING_NOTIFICATIONS, value)?.apply()
            }

        var messagingToken: String
            get() {
                return sharedPreferences?.getString(FIREBASE_MESSAGING_TOKEN, null)!!
            }
            set(value) {
                sharedPreferences?.edit()?.putString(FIREBASE_MESSAGING_TOKEN, value)?.apply()
            }
    }
}