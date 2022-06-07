package com.spevsand.muemitter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.media.audiofx.Equalizer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.spevsand.muemitter.databinding.EntityEqualizerBarBinding
import com.spevsand.muemitter.constants.Constants.EQUALIZER_SAVED_BAND_LEVEL_
import com.spevsand.muemitter.constants.Constants.EQUALIZER_SHARED_PREF

class EqualizerBandsAdapter(private val equalizer: Equalizer) :
    RecyclerView.Adapter<EqualizerBandsAdapter.EqualizerBandView>() {

    private lateinit var binding: EntityEqualizerBarBinding

    inner class EqualizerBandView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
        fun bind(position: Int) {
            val upperBandRange = equalizer.bandLevelRange[1]
            val centerFreq = equalizer.getCenterFreq(position.toShort())
            val lowerBandRange = equalizer.bandLevelRange[0]
            //paintWithSavedColor(binding.dialogEqualizerEntityMain)
            binding.dialogEqualizerUpperBand.text = "${upperBandRange / 100} dB"
            binding.dialogEqualizerCenterFreq.text = "${centerFreq / 1000} Hz"
            binding.dialogEqualizerLowerBand.text = "${lowerBandRange / 100} dB"
            Log.e("1", "0")
            binding.dialogEqualizerSeekBar.apply {
                max = upperBandRange.toInt() - lowerBandRange.toInt()
                progress = itemView.context.getSharedPreferences(
                    EQUALIZER_SHARED_PREF,
                    Context.MODE_PRIVATE
                ).getInt(EQUALIZER_SAVED_BAND_LEVEL_ + centerFreq, max / 2) - lowerBandRange.toInt()
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        itemView.context.getSharedPreferences(
                            EQUALIZER_SHARED_PREF,
                            Context.MODE_PRIVATE
                        ).edit().putInt(
                            EQUALIZER_SAVED_BAND_LEVEL_ + centerFreq, (lowerBandRange + p1)
                        ).apply()
                        //Log.e("BBB $position", (lowerBandRange + p1).toString())
                        equalizer.setBandLevel(position.toShort(), (lowerBandRange + p1).toShort())
                        Log.e("1", "1")
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {}

                    override fun onStopTrackingTouch(p0: SeekBar?) {}

                })
                setOnTouchListener(View.OnTouchListener { p0, p1 ->
                    when (p1?.action) {
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_DOWN -> p0.parent.requestDisallowInterceptTouchEvent(
                            true
                        )
                    }
                    p0.onTouchEvent(p1)
                    return@OnTouchListener true
                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EqualizerBandView {
        binding =
            EntityEqualizerBarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EqualizerBandView(binding.root)
    }

    override fun onBindViewHolder(holder: EqualizerBandView, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = equalizer.numberOfBands.toInt()

    override fun getItemId(position: Int): Long = position.toLong() //TODO ?

}