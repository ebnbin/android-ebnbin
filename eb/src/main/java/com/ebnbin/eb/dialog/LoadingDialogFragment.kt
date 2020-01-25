package com.ebnbin.eb.dialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.ebnbin.eb.databinding.EbLoadingDialogFragmentBinding
import com.ebnbin.eb.layoutInflater

open class LoadingDialogFragment : DialogFragment() {
    private lateinit var binding: EbLoadingDialogFragmentBinding

    override fun onDialogCreated(dialog: AppCompatDialog, savedInstanceState: Bundle?) {
        super.onDialogCreated(dialog, savedInstanceState)
        binding = EbLoadingDialogFragmentBinding.inflate(requireContext().layoutInflater)
        dialog.setContentView(binding.root)
    }
}
