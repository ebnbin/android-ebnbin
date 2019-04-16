package com.ebnbin.eb.loading

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.R
import com.ebnbin.eb.app.EBDialogFragment

class LoadingDialogFragment : EBDialogFragment() {
    private lateinit var callback: Callback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = attachCallbackNotNull(Callback::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AppCompatDialog(requireContext())
        dialog.setContentView(R.layout.eb_loading_dialog_fragment)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback.onLoadingDialogDismiss()
    }

    interface Callback {
        fun onLoadingDialogDismiss()
    }

    companion object {
        fun start(fm: FragmentManager, isCancelable: Boolean): LoadingDialogFragment {
            val fragment = LoadingDialogFragment()
            fragment.isCancelable = isCancelable
            fragment.show(fm, LoadingDialogFragment::class.java.name)
            return fragment
        }
    }
}
