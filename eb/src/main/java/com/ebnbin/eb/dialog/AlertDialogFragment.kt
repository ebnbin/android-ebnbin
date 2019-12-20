package com.ebnbin.eb.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import com.ebnbin.eb.fragment.getCallback
import com.ebnbin.eb.fragment.requireArgument
import com.ebnbin.eb.util.KEY_CALLBACK_BUNDLE
import com.google.android.material.dialog.MaterialAlertDialogBuilder

open class AlertDialogFragment : EBDialogFragment() {
    interface Callback {
        /**
         * @return 点击按钮是否 dismiss.
         */
        fun onAlertDialogPositive(alertDialog: AlertDialog, callbackBundle: Bundle): Boolean = true

        /**
         * @return 点击按钮是否 dismiss.
         */
        fun onAlertDialogNegative(alertDialog: AlertDialog, callbackBundle: Bundle): Boolean = true

        /**
         * @return 点击按钮是否 dismiss.
         */
        fun onAlertDialogNeutral(alertDialog: AlertDialog, callbackBundle: Bundle): Boolean = true

        fun onAlertDialogShow(alertDialog: AlertDialog, callbackBundle: Bundle) = Unit

        fun onAlertDialogCancel(alertDialog: AlertDialog, callbackBundle: Bundle) = Unit

        fun onAlertDialogDismiss(alertDialog: AlertDialog, callbackBundle: Bundle) = Unit
    }

    protected open val isMaterial: Boolean
        get() = requireArgument(KEY_IS_MATERIAL)
    protected open val title: CharSequence?
        get() = requireArgument(KEY_TITLE)
    protected open val message: CharSequence?
        get() = requireArgument(KEY_MESSAGE)
    protected open val positiveText: CharSequence?
        get() = requireArgument(KEY_POSITIVE_TEXT)
    protected open val negativeText: CharSequence?
        get() = requireArgument(KEY_NEGATIVE_TEXT)
    protected open val neutralText: CharSequence?
        get() = requireArgument(KEY_NEUTRAL_TEXT)
    protected open val dialogCancelable: DialogCancelable
        get() = requireArgument(KEY_DIALOG_CANCELABLE)
    protected open val callbackBundle: Bundle
        get() = requireArgument(KEY_CALLBACK_BUNDLE)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = if (isMaterial) {
            MaterialAlertDialogBuilder(requireContext())
        } else {
            AlertDialog.Builder(requireContext())
        }.setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText, null)
            .setNegativeButton(negativeText, null)
            .setNeutralButton(neutralText, null)
        val dialog = builder.create()
        dialog.setOnShowListener {
            val alertDialog = it as AlertDialog
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
                if (onAlertDialogPositive(alertDialog)) {
                    dismissAllowingStateLoss()
                }
            }
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setOnClickListener {
                if (onAlertDialogNegative(alertDialog)) {
                    dismissAllowingStateLoss()
                }
            }
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.setOnClickListener {
                if (onAlertDialogNeutral(alertDialog)) {
                    dismissAllowingStateLoss()
                }
            }
            onAlertDialogShow(alertDialog)
        }
        val dialogCancelable = dialogCancelable
        dialog.setCanceledOnTouchOutside(dialogCancelable == DialogCancelable.CANCELABLE)
        isCancelable = dialogCancelable != DialogCancelable.NOT_CANCELABLE
        return dialog
    }

    /**
     * @return 点击按钮是否 dismiss.
     */
    protected open fun onAlertDialogPositive(alertDialog: AlertDialog): Boolean {
        return getCallback<Callback>()?.onAlertDialogPositive(alertDialog, callbackBundle) != false
    }

    /**
     * @return 点击按钮是否 dismiss.
     */
    protected open fun onAlertDialogNegative(alertDialog: AlertDialog): Boolean {
        return getCallback<Callback>()?.onAlertDialogNegative(alertDialog, callbackBundle) != false
    }

    /**
     * @return 点击按钮是否 dismiss.
     */
    protected open fun onAlertDialogNeutral(alertDialog: AlertDialog): Boolean {
        return getCallback<Callback>()?.onAlertDialogNeutral(alertDialog, callbackBundle) != false
    }

    protected open fun onAlertDialogShow(alertDialog: AlertDialog) {
        getCallback<Callback>()?.onAlertDialogShow(alertDialog, callbackBundle)
    }

    protected open fun onAlertDialogCancel(alertDialog: AlertDialog) {
        getCallback<Callback>()?.onAlertDialogCancel(alertDialog, callbackBundle)
    }

    protected open fun onAlertDialogDismiss(alertDialog: AlertDialog) {
        getCallback<Callback>()?.onAlertDialogDismiss(alertDialog, callbackBundle)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onAlertDialogCancel(dialog as AlertDialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onAlertDialogDismiss(dialog as AlertDialog)
    }

    companion object {
        private const val KEY_IS_MATERIAL: String = "is_material"
        private const val KEY_TITLE: String = "title"
        private const val KEY_MESSAGE: String = "message"
        private const val KEY_POSITIVE_TEXT: String = "positive_text"
        private const val KEY_NEGATIVE_TEXT: String = "negative_text"
        private const val KEY_NEUTRAL_TEXT: String = "neutral_text"
        private const val KEY_DIALOG_CANCELABLE: String = "dialog_cancelable"

        fun createArguments(
            /**
             * 是否使用 MaterialAlertDialogBuilder.
             */
            isMaterial: Boolean = true,
            title: CharSequence? = null,
            message: CharSequence? = null,
            positiveText: CharSequence? = null,
            negativeText: CharSequence? = null,
            neutralText: CharSequence? = null,
            dialogCancelable: DialogCancelable = DialogCancelable.CANCELABLE,
            callbackBundle: Bundle = Bundle.EMPTY
        ): Bundle {
            return bundleOf(
                KEY_IS_MATERIAL to isMaterial,
                KEY_TITLE to title,
                KEY_MESSAGE to message,
                KEY_POSITIVE_TEXT to positiveText,
                KEY_NEGATIVE_TEXT to negativeText,
                KEY_NEUTRAL_TEXT to neutralText,
                KEY_DIALOG_CANCELABLE to dialogCancelable,
                KEY_CALLBACK_BUNDLE to callbackBundle
            )
        }
    }
}
