package com.spevsand.muemitter.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spevsand.muemitter.databinding.EntityMessageBinding
import com.spevsand.muemitter.model.Message
import com.spevsand.muemitter.constants.Constants.paintWithSavedColor
import com.spevsand.muemitter.constants.UserConstants.appFirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class MessagesListAdapter : ListAdapter<Message, MessagesListAdapter.MessageHolder>(
    DIFF_UTIL
) {
    private lateinit var binding: EntityMessageBinding

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.name == newItem.name && oldItem.body == newItem.body && oldItem.unixTime == newItem.unixTime
            }

        }
    }

    inner class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val item = getItem(position)
            paintWithSavedColor(binding.messageFrame)
            binding.messageSenderTextView.text = item.name
            binding.messageTimeTextView.text = properTime(item.unixTime)
            binding.messageBodyTextView.text = item.body
            if (item.senderUid == appFirebaseAuth.uid) binding.messageMyMessage.visibility =
                View.VISIBLE
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun properTime(unixTime: Long): String {
        return SimpleDateFormat("[kk:mm:ss] dd/MM/yy").format(Date(unixTime))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        binding = EntityMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int) = position

}