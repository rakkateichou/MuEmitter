package com.spevsand.muemitter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spevsand.muemitter.R
import com.spevsand.muemitter.databinding.EntityPlaylistBinding
import com.spevsand.muemitter.model.AudioData
import com.spevsand.muemitter.constants.Constants.paintWithSavedColor

class PlaylistAdapter : ListAdapter<AudioData, PlaylistAdapter.AudioDataHolder>(DIFF_CALLBACK) {
    private lateinit var binding: EntityPlaylistBinding

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AudioData>() {
            override fun areItemsTheSame(oldItem: AudioData, newItem: AudioData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AudioData, newItem: AudioData): Boolean {
                return oldItem.audio_max_length == newItem.audio_max_length
                        && oldItem.audio_title == newItem.audio_title
                        && oldItem.audio_owner == newItem.audio_owner
                        && oldItem.audio_url == newItem.audio_url
            }

        }
    }

    inner class AudioDataHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            paintWithSavedColor(binding.playlistEntityFrame)
            binding.playlistEntityAudioTitle.text = getItem(position).audio_title
            binding.playlistEntityAudioOwner.text =
                itemView.context.getString(R.string.uploaded_by, getItem(position).audio_owner)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioDataHolder {
        binding = EntityPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AudioDataHolder(binding.root)
    }

    override fun onBindViewHolder(holder: AudioDataHolder, position: Int) {
        holder.bind(position)
    }
}