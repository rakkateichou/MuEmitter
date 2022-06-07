package com.spevsand.muemitter.dialog

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spevsand.muemitter.databinding.LayoutUpdaterSheetBinding
import com.spevsand.muemitter.constants.Constants.paintWithSavedColor

class UpdaterBottomSheetDialog(
    val activity: Any,
    private val oldVer: String,
    private val newVer: String
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: LayoutUpdaterSheetBinding
    private var mListener: UpdateSheetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutUpdaterSheetBinding.inflate(inflater, container, false)
        val yesButton = binding.updaterSheetYes
        val noButton = binding.updaterSheetNo
        binding.updaterSheetOldVer.text = oldVer
        binding.updaterSheetNewVer.text = newVer
        paintWithSavedColor(binding.updaterSheetMainLayout)
        yesButton.setOnClickListener {
            mListener!!.onUpdateRequested()
            dismiss()
        }
        noButton.setOnClickListener { dismiss() }
        return binding.root
    }

    interface UpdateSheetListener {
        fun onUpdateRequested()
    }

    override fun onAttach(_activity: Activity) {
        super.onAttach(_activity)
        mListener = activity as UpdateSheetListener
    }

}