package com.ebnbin.eb.async

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.R
import com.ebnbin.eb.app.EBDialogFragment
import com.ebnbin.eb.dialog.Cancel
import com.ebnbin.eb.util.Consts

/**
 * 加载中对话框.
 */
class LoadingDialogFragment : EBDialogFragment() {
    private lateinit var callback: Callback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = attachCallbackNotNull(Callback::class.java)
    }

    private lateinit var cancel: Cancel
    private lateinit var extraData: Bundle

    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        cancel = arguments.getSerializable("cancel") as Cancel
        extraData = arguments.getBundle(Consts.EXTRA_DATA) ?: throw RuntimeException()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = cancel != Cancel.NOT_CANCELABLE
        val dialog = AppCompatDialog(requireContext())
        dialog.setContentView(R.layout.eb_loading_dialog_fragment)
        dialog.setCanceledOnTouchOutside(cancel == Cancel.CANCELABLE)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback.onLoadingDialogDismiss(cancel, extraData)
    }

    interface Callback {
        fun onLoadingDialogDismiss(cancel: Cancel, extraData: Bundle)
    }

    companion object {
        fun start(fm: FragmentManager, cancel: Cancel, extraData: Bundle = Bundle.EMPTY): LoadingDialogFragment {
            val fragment = LoadingDialogFragment()
            fragment.arguments = bundleOf(
                "cancel" to cancel,
                Consts.EXTRA_DATA to extraData
            )
            fragment.show(fm, LoadingDialogFragment::class.java.name)
            return fragment
        }
    }
}
