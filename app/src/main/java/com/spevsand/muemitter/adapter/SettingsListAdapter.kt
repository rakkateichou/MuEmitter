package com.spevsand.muemitter.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spevsand.muemitter.R
import com.spevsand.muemitter.activity.ChangeThemeActivity
import com.spevsand.muemitter.activity.EditProfileActivity
import com.spevsand.muemitter.activity.NotificationsSettingsActivity
import com.spevsand.muemitter.databinding.EntitySettingsBinding
import com.spevsand.muemitter.dialog.LogoutDialog
import com.spevsand.muemitter.constants.Constants.paintWithSavedColor

class SettingsListAdapter(val mActivity: Activity) :
    RecyclerView.Adapter<SettingsListAdapter.SettingsEntity>() {

    private lateinit var binding: EntitySettingsBinding

    val arrayOfEntities = arrayListOf(
        R.string.settings_edit_profile,
        R.string.settings_notifications,
        R.string.settings_change_theme,
        R.string.settings_logout
    )
    val arrayOfEntitiesAct = arrayListOf(
        EditProfileActivity::class.java,
        NotificationsSettingsActivity::class.java,
        ChangeThemeActivity::class.java
    )

    inner class SettingsEntity(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var position: Int? = null
        fun bind(position: Int) {
            this.position = position
            paintWithSavedColor(binding.settingsEntityFrame)
            binding.settingsEntityTitle.text =
                itemView.context.getString(arrayOfEntities[position])
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if (position != 3) {
                itemView.context.startActivity(
                    Intent(
                        itemView.context,
                        arrayOfEntitiesAct[position!!]
                    )
                )
            } else {
                LogoutDialog(mActivity).show()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsEntity {
        binding = EntitySettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SettingsEntity(binding.root)
    }

    override fun onBindViewHolder(holder: SettingsEntity, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = arrayOfEntities.size
}