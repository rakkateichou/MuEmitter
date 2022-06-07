package com.spevsand.muemitter.autoupdate

import android.os.AsyncTask
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.spevsand.muemitter.dialog.UpdaterBottomSheetDialog
import com.spevsand.muemitter.constants.Constants.SHEET_TAG_UPDATE
import com.spevsand.muemitter.constants.Constants.appRTDBReference
import java.lang.Exception

class Updater(val activity: AppCompatActivity) : AsyncTask<Void, Int, Void>(),
    UpdaterBottomSheetDialog.UpdateSheetListener {

    override fun doInBackground(vararg p0: Void?): Void? {
        appRTDBReference.child("app_last_version").child("name")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val oldVer = activity.packageManager.getPackageInfo(activity.packageName, 0).versionName
                    val newVer = snapshot.getValue(String::class.java)
                    if (oldVer != newVer) {
                        UpdaterBottomSheetDialog(this@Updater, oldVer, newVer!!).show(
                            activity.supportFragmentManager,
                            SHEET_TAG_UPDATE
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return null
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
    }

    override fun onUpdateRequested() {
        appRTDBReference.child("app_last_version").child("url")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        DownloadController(
                            activity,
                            snapshot.getValue(String::class.java)!!
                        ).enqueueDownload()
                    } catch (e: Exception){
                        Toast.makeText(activity, "An server exception occurred", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}