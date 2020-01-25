package com.ebnbin.eb.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import com.ebnbin.eb.fragment.FragmentCallback
import com.ebnbin.eb.fragment.getArgumentOrDefault
import com.ebnbin.eb.fragment.getCallback

abstract class DialogFragment : AppCompatDialogFragment() {
    protected open val dialogCancelable: DialogCancelable
        get() = getArgumentOrDefault(KEY_DIALOG_CANCELABLE, DialogCancelable.CANCELABLE)
    protected open val fragmentCallback: FragmentCallback
        get() = getArgumentOrDefault(KEY_FRAGMENT_CALLBACK, FragmentCallback.PREFER_PARENT_FRAGMENT)
    protected open val callbackBundle: Bundle
        get() = getArgumentOrDefault(KEY_CALLBACK_BUNDLE, Bundle.EMPTY)

    //*****************************************************************************************************************

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AppCompatDialog(requireContext(), theme)
        dialog.setOnShowListener {
            it as AppCompatDialog
            onDialogShow(it)
        }
        dialog.setCanceledOnTouchOutside(dialogCancelable == DialogCancelable.CANCELABLE)
        isCancelable = dialogCancelable != DialogCancelable.NOT_CANCELABLE
        onCreateDialog(dialog)
        return dialog
    }

    //*****************************************************************************************************************

    protected val appCompatDialog: AppCompatDialog?
        get() = dialog as AppCompatDialog?

    protected fun requireAppCompatDialog(): AppCompatDialog {
        return appCompatDialog ?: throw IllegalStateException("无法获取 AppCompatDialog.")
    }

    //*****************************************************************************************************************

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onDialogCancel(dialog as AppCompatDialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDismiss(dialog as AppCompatDialog)
    }

    //*****************************************************************************************************************

    protected open fun onCreateDialog(dialog: AppCompatDialog) {
        getCallback<Callback>(fragmentCallback)?.onCreateDialog(dialog, callbackBundle)
    }

    protected open fun onDialogShow(dialog: AppCompatDialog) {
        getCallback<Callback>(fragmentCallback)?.onDialogShow(dialog, callbackBundle)
    }

    protected open fun onDialogCancel(dialog: AppCompatDialog) {
        getCallback<Callback>(fragmentCallback)?.onDialogCancel(dialog, callbackBundle)
    }

    protected open fun onDialogDismiss(dialog: AppCompatDialog) {
        getCallback<Callback>(fragmentCallback)?.onDialogDismiss(dialog, callbackBundle)
    }

    //*****************************************************************************************************************

    interface Callback {
        fun onCreateDialog(dialog: AppCompatDialog, callbackBundle: Bundle) = Unit

        fun onDialogShow(dialog: AppCompatDialog, callbackBundle: Bundle) = Unit

        fun onDialogCancel(dialog: AppCompatDialog, callbackBundle: Bundle) = Unit

        fun onDialogDismiss(dialog: AppCompatDialog, callbackBundle: Bundle) = Unit
    }

    //*****************************************************************************************************************

    companion object {
        const val KEY_DIALOG_CANCELABLE: String = "dialog_cancelable"
        const val KEY_FRAGMENT_CALLBACK = "fragment_callback"
        const val KEY_CALLBACK_BUNDLE = "callback_bundle"

        fun createArguments(
            dialogCancelable: DialogCancelable = DialogCancelable.CANCELABLE,
            fragmentCallback: FragmentCallback = FragmentCallback.PREFER_PARENT_FRAGMENT,
            callbackBundle: Bundle = Bundle.EMPTY
        ): Bundle {
            return bundleOf(
                KEY_DIALOG_CANCELABLE to dialogCancelable,
                KEY_FRAGMENT_CALLBACK to fragmentCallback,
                KEY_CALLBACK_BUNDLE to callbackBundle
            )
        }
    }
}
