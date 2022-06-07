package com.spevsand.muemitter.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.audiofx.Equalizer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.spevsand.muemitter.R
import com.spevsand.muemitter.adapter.MessagesListAdapter
import com.spevsand.muemitter.adapter.PlaylistAdapter
import com.spevsand.muemitter.adapter.SettingsListAdapter
import com.spevsand.muemitter.autoupdate.Updater
import com.spevsand.muemitter.databinding.ActivityMainBinding
import com.spevsand.muemitter.dialog.AudioEqualizerBottomSheetDialog
import com.spevsand.muemitter.model.AudioData
import com.spevsand.muemitter.constants.Constants.EQUALIZER_SAVED_BAND_LEVEL_
import com.spevsand.muemitter.constants.Constants.EQUALIZER_SHARED_PREF
import com.spevsand.muemitter.constants.Constants.EXOPLAYER_CUR_VOLUME
import com.spevsand.muemitter.constants.Constants.EXOPLAYER_IS_MUTE
import com.spevsand.muemitter.constants.Constants.SHARED_PREF_EXOPLAYER
import com.spevsand.muemitter.constants.Constants.SHEET_TAG_EQUALIZER
import com.spevsand.muemitter.constants.Constants.appRTDBReference
import com.spevsand.muemitter.constants.Constants.appStorageRoomReference
import com.spevsand.muemitter.constants.Constants.paintWithSavedColor
import com.spevsand.muemitter.constants.UserConstants.user
import com.spevsand.muemitter.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() { //TODO: EVERYTHING IS BAD HERE

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private lateinit var mToolbar: View

    private lateinit var mAudioTitle: TextView
    private lateinit var mAudioOwner: TextView
    private lateinit var mAudioTextTime: TextView
    private lateinit var mAudioSeekBar: SeekBar
    private lateinit var mAudioMuteButton: ImageView
    private lateinit var mAudioEqualizerButton: ImageView

    private lateinit var mMessageRecyclerView: RecyclerView
    private lateinit var mSendButton: TextView
    private lateinit var mEditMessage: EditText

    private lateinit var mPlaylistRecyclerView: RecyclerView
    private lateinit var mPlaylistTextView: TextView
    private lateinit var mPlaylistButton: Button
    private lateinit var mPlaylistProgressBar: ProgressBar

    private lateinit var mSettingsRecyclerView: RecyclerView

    private lateinit var exoPlayer: SimpleExoPlayer

    private var lastAudioDataId: String = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Updater(this).execute()

        mToolbar = binding.includeLayoutChatToolbar.root
        mAudioTitle = binding.includeLayoutChatToolbar.toolbarAudioTitle
        mAudioOwner = binding.includeLayoutChatToolbar.toolbarAudioOwner
        mAudioTextTime = binding.includeLayoutChatToolbar.toolbarAudioTime
        mAudioSeekBar = binding.includeLayoutChatToolbar.toolbarAudioProgress
        mAudioSeekBar.setOnTouchListener { _, _ -> true }
        mAudioMuteButton = binding.includeLayoutChatToolbar.toolbarAudioSilentMode
        mAudioEqualizerButton = binding.includeLayoutChatToolbar.toolbarAudioEqualizer

        mSettingsRecyclerView = binding.includeLayoutSettings.settingsSliderRecyclerview
        mSettingsRecyclerView.setHasFixedSize(true)

        mMessageRecyclerView = binding.messageAreaRecyclerview
        mMessageRecyclerView.setHasFixedSize(true)
        mSendButton = binding.sendButton
        mEditMessage = binding.myMessageEdittext

        mPlaylistRecyclerView = binding.includeLayoutPlaylist.playlistSliderRecyclerview
        mPlaylistRecyclerView.setHasFixedSize(true)
        mPlaylistTextView = binding.includeLayoutPlaylist.playlistSliderTextview
        mPlaylistButton = binding.includeLayoutPlaylist.playlistSliderAddAudioButton
        mPlaylistProgressBar = binding.includeLayoutPlaylist.playlistSliderProgressBar

        val mSettingsLayoutManager = LinearLayoutManager(this)
        mSettingsRecyclerView.layoutManager = mSettingsLayoutManager
        val mSettingsAdapter =
            SettingsListAdapter(this)
        mSettingsRecyclerView.adapter = mSettingsAdapter

        val mMessagesLayoutManager = LinearLayoutManager(this)
        mMessagesLayoutManager.stackFromEnd = true
        mMessageRecyclerView.layoutManager = mMessagesLayoutManager
        val mMessagesAdapter = MessagesListAdapter()
        mMessagesAdapter.setHasStableIds(true)
        mMessageRecyclerView.adapter = mMessagesAdapter

        val mPlaylistLayoutManager = LinearLayoutManager(this)
        mPlaylistRecyclerView.layoutManager = mPlaylistLayoutManager
        val mPlaylistAdapter = PlaylistAdapter()
        mPlaylistRecyclerView.adapter = mPlaylistAdapter

        exoPlayer = SimpleExoPlayer.Builder(this).build().also { it.playWhenReady = true }

        mPlaylistButton.setOnClickListener {
            Intent().apply {
                this.action = Intent.ACTION_GET_CONTENT
                this.type = "audio/mpeg"
                startActivityForResult(
                    Intent.createChooser(
                        this,
                        getString(R.string.select_an_mp3)
                    ), 0
                )
            }
        }

        mAudioMuteButton.setOnClickListener {
            getSharedPreferences(SHARED_PREF_EXOPLAYER, MODE_PRIVATE).apply {
                if (!this.getBoolean(EXOPLAYER_IS_MUTE, false)) {
                    this.edit().putFloat(EXOPLAYER_CUR_VOLUME, exoPlayer.volume).apply()
                    this.edit().putBoolean(EXOPLAYER_IS_MUTE, true).apply()
                    exoPlayer.volume = 0f
                    mAudioMuteButton.background =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_volume_off)
                    paintWithSavedColor(mAudioMuteButton as View)
                } else {
                    this.edit().putBoolean(EXOPLAYER_IS_MUTE, false).apply()
                    exoPlayer.volume = this.getFloat(EXOPLAYER_CUR_VOLUME, 1f)
                    mAudioMuteButton.background =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_volume_up)
                    paintWithSavedColor(mAudioMuteButton as View)
                }
            }
        }


        exoPlayer.addAnalyticsListener(object : AnalyticsListener {
            override fun onAudioSessionIdChanged(
                eventTime: AnalyticsListener.EventTime,
                audioSessionId: Int
            ) {
                super.onAudioSessionIdChanged(eventTime, audioSessionId)
                val equalizer = Equalizer(0, audioSessionId).apply { this.enabled = true }
                mAudioEqualizerButton.setOnClickListener {
                    Log.e("123", "1")
                    AudioEqualizerBottomSheetDialog(
                        equalizer
                    ).show(
                        supportFragmentManager,
                        SHEET_TAG_EQUALIZER
                    )
                }
                for (bandIndex in 0 until equalizer.numberOfBands) {
                    val bandProgress =
                        getSharedPreferences(EQUALIZER_SHARED_PREF, Context.MODE_PRIVATE).getInt(
                            EQUALIZER_SAVED_BAND_LEVEL_ + equalizer.getCenterFreq(bandIndex.toShort()),
                            0
                        ).toShort()
                    //Log.e("AAA $bandIndex", bandProgress.toString())
                    equalizer.setBandLevel(
                        bandIndex.toShort(),
                        bandProgress
                    )
                }
            }
        })

        viewModel.userSnapshotLiveData.observe(this) {
            user = it
            if (!mSendButton.hasOnClickListeners()) {
                mSendButton.setOnClickListener {
                    if (mEditMessage.text.toString().isNotBlank()) {
                        viewModel.passMessageText(mEditMessage.text.toString().trim())
                        mEditMessage.text.clear()
                    }
                }
            }
        }

        viewModel.mediatorPlaylistLiveData.observe(this) {
            when {
                it[0].id != lastAudioDataId -> {
                    setCurrentAudioInfo(it[0])
                    mPlaylistAdapter.submitList(it)
                    mPlaylistTextView.text = getString(R.string.playlist, it.size)
                }
                it[0].id.isEmpty() -> {
                    it[0].audio_title = getString(R.string.no_audio_is_playing)
                    setCurrentAudioInfo(it[0])
                }
                else -> {
                    mPlaylistAdapter.submitList(it)
                    mPlaylistTextView.text = getString(R.string.playlist, it.size)
                }
            }
        }

        viewModel.mediatorMessagesLiveData.observe(this) {
            mMessagesAdapter.submitList(it)
            //Log.e("NMMMM", mMessagesLayoutManager.findLastCompletelyVisibleItemPosition().toString())
            //Log.e("NMMMM", it.size.toString())
            //if (mMessagesLayoutManager.findLastCompletelyVisibleItemPosition() == (it.size - 2)) {
            //mMessagesLayoutManager.scrollToPosition(it.size - 1) //todo new messages indicator
            //}
        }

        super.onCreate(savedInstanceState)
    }

    private fun setCurrentAudioInfo(audioData: AudioData) {
        exoPlayer.prepare(buildMediaSource(Uri.parse(audioData.audio_url))!!)
        mAudioSeekBar.max = audioData.audio_max_length
        exoPlayer.seekTo(audioData.audio_time.toLong())
        mAudioSeekBar.progress = audioData.audio_time
        mAudioTitle.text = audioData.audio_title
        mAudioTitle.isSelected = true
        mAudioOwner.text = getString(R.string.uploaded_by, audioData.audio_owner)
        mAudioTextTime.text = getString(
            R.string.audio_time_template,
            properTime(audioData.audio_time),
            properTime(audioData.audio_max_length)
        )
        lastAudioDataId = audioData.id
    }

    private fun properTime(timeIn: Int): String {
        var temp = (timeIn / 1000 / 60).toString()
        val minutes = if (temp.length == 1) "0$temp" else temp
        temp = (timeIn / 1000 % 60).toString()
        val seconds = if (temp.length == 1) "0$temp" else temp
        return "$minutes:$seconds"
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this, "exoplayer-muemitter")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) { // TODO: this is ESPECIALLY bad
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {
                try {
                    val audioFileUri: Uri? = data.data
                    val mr = MediaMetadataRetriever()
                    mr.setDataSource(this, audioFileUri)
                    val fileName = audioFileUri?.lastPathSegment!!.substring(
                        0,
                        audioFileUri.lastPathSegment!!.lastIndexOf('.')
                    ) // TODO Only one usage
                    val maxLen =
                        mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toInt()
                    mr.release()
                    val timeMs = System.currentTimeMillis().toString()
                    appStorageRoomReference.child(timeMs).putFile(audioFileUri)
                        .addOnSuccessListener { it ->
                            val handler = Handler()
                            handler.postDelayed({ mPlaylistProgressBar.progress = 0 }, 500)
                            it.storage.downloadUrl.addOnSuccessListener {
                                appRTDBReference.child("playlist").child(
                                    timeMs
                                )
                                    .setValue(
                                        AudioData(
                                            fileName,
                                            user.username,
                                            maxLen,
                                            it.toString()
                                        )
                                    )
                            }.addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "An error occurred. Line: 292",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(this, "An error occurred. Line: 297", Toast.LENGTH_LONG)
                                .show()
                        }.addOnProgressListener {
                            mPlaylistProgressBar.progress =
                                (100.0 * it.bytesTransferred / it.totalByteCount).toInt()
                        }
                } catch (e: Exception) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        paintWithSavedColor(binding.mainLayout)
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }
}