package com.ebnbin.eb.permission

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.R
import com.ebnbin.eb.dialog.EBDialogFragment
import com.ebnbin.eb.fragment.FragmentHelper
import com.ebnbin.eb.util.Consts

internal class PermissionDialogFragment : EBDialogFragment() {
    private lateinit var callback: Callback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = attachCallbackNotNull(Callback::class.java)
    }

    interface Callback {
        fun onOk(extraData: Bundle)
    }

    private lateinit var message: String
    private lateinit var extraData: Bundle

    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        message = arguments.getString("message") ?: throw RuntimeException()
        extraData = arguments.getBundle(Consts.KEY_EXTRA_DATA) ?: throw RuntimeException()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.eb_permission_dialog_positive) { _, _ ->
                callback.onOk(extraData)
            }
            .create()
    }

    companion object {
        fun start(fm: FragmentManager, message: String, extraData: Bundle = Bundle.EMPTY): PermissionDialogFragment {
            return FragmentHelper.add(fm, PermissionDialogFragment::class.java, arguments = bundleOf(
                "message" to message,
                Consts.KEY_EXTRA_DATA to extraData
            ))
        }
    }
}
