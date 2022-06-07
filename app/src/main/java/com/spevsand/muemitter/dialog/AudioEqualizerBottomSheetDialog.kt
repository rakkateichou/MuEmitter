package com.spevsand.muemitter.dialog

import android.content.Context
import android.media.audiofx.Equalizer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spevsand.muemitter.adapter.EqualizerBandsAdapter
import com.spevsand.muemitter.databinding.LayoutEqualizerSheetBinding
import com.spevsand.muemitter.constants.Constants

class AudioEqualizerBottomSheetDialog(private val equalizer: Equalizer) :
    BottomSheetDialogFragment() {
    private lateinit var binding: LayoutEqualizerSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutEqualizerSheetBinding.inflate(inflater, container, false)
        Log.e("12345", "12345")
        val view = binding.root
        Log.e("0", "0")
        binding.dialogEqualizerRecyclerView.apply {
            Log.e("0", "1")
            this.layoutManager =
                LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            Log.e("0", "2")
            this.adapter = EqualizerBandsAdapter(equalizer)
            Log.e("0", "3")
            //this.adapter?.setHasStableIds(true)
            Log.e("0", "4")
        }
        Log.e("6", "6")
        binding.dialogEqualizerBackToDefaultsButton.setOnClickListener {
            for (bandIndex in 0 until equalizer.numberOfBands) {
                view.context.getSharedPreferences(
                    Constants.EQUALIZER_SHARED_PREF,
                    Context.MODE_PRIVATE
                ).edit().putInt(
                    Constants.EQUALIZER_SAVED_BAND_LEVEL_ + equalizer.getCenterFreq(bandIndex.toShort()),
                    0
                ).apply()
                equalizer.setBandLevel(
                    bandIndex.toShort(),
                    0
                )
            }
            binding.dialogEqualizerRecyclerView.adapter?.notifyDataSetChanged()
        }
        Log.e("7", "7")
        //paintWithSavedColor(view)
        return view
    }
}