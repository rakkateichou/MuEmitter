package com.spevsand.muemitter.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.spevsand.muemitter.databinding.ActivityNotificationsSettingsBinding
import com.spevsand.muemitter.notifications.AppMessagingService
import com.spevsand.muemitter.notifications.AppMessagingService.Companion.SHARED_PREF_NOTIFICATIONS
import com.spevsand.muemitter.notifications.AppMessagingService.Companion.isReceivingNotifications
import com.spevsand.muemitter.constants.Constants.paintWithSavedColor

class NotificationsSettingsActivity : AppCompatActivity() {

    private lateinit var mAllNotificationsSwitch: SwitchCompat
    private lateinit var binding: ActivityNotificationsSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNotificationsSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        paintWithSavedColor(binding.activityNotificationsMain)

        mAllNotificationsSwitch = binding.activityNotificationsReceiveSwitch
        mAllNotificationsSwitch.setOnCheckedChangeListener { _, boolean ->
            isReceivingNotifications = boolean
        }

        AppMessagingService.sharedPreferences ?: run {
            AppMessagingService.sharedPreferences =
                getSharedPreferences(SHARED_PREF_NOTIFICATIONS, Context.MODE_PRIVATE)
        }
    }
}