package com.spevsand.muemitter.constants

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.spevsand.muemitter.view.Colorable

object Constants {
    val appRTDBReference = Firebase.database.reference
    val appFirestoreReference = Firebase.firestore
    val appStorageRoomReference = Firebase.storage.getReference("/Main Room")

    const val SHARED_PREF_COLOR = "shared_pref_color"

    const val APP_COLOR = "app_color"

    const val SHARED_PREF_EXOPLAYER = "shared_pref_exoplayer"

    const val EXOPLAYER_IS_MUTE = "exoplayer_is_mute"
    const val EXOPLAYER_CUR_VOLUME = "exoplayer_cur_volume"

    const val SHEET_TAG_UPDATE = "updaterSheet"
    const val SHEET_TAG_EQUALIZER = "equalizerSheet"

    const val EQUALIZER_SHARED_PREF = "equalizer_shared_pref"

    const val EQUALIZER_SAVED_BAND_LEVEL_ = "equalizer_saved_band_level_"

    var notToken: String? = null

    fun paintWithSavedColor(parent: ViewGroup): Int {
        with(
            parent.context.getSharedPreferences(SHARED_PREF_COLOR, Context.MODE_PRIVATE).getInt(
                APP_COLOR,
                Color.WHITE
            )
        ) {
            if(parent is Colorable) parent.color(this)
            for (child in parent.children) {
                if (child is ViewGroup) paintWithSavedColor(child)
                if (child is Colorable) child.color(this)
            }
            return this
        }
    }

    fun paintWithSavedColor(view: View): Int {
        with(
            view.context.getSharedPreferences(SHARED_PREF_COLOR, Context.MODE_PRIVATE).getInt(
                APP_COLOR,
                Color.WHITE
            )
        ) {
            if(view is Colorable) view.color(this)
            return this
        }
    }

    fun saveColor(context: Context, color: Int) {
        with(context.getSharedPreferences(SHARED_PREF_COLOR, Context.MODE_PRIVATE).edit()) {
            putInt(APP_COLOR, color)
            commit()
        }
    }
}