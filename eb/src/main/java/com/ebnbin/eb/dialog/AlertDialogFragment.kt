package com.ebnbin.eb.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import com.ebnbin.eb.FragmentCallback
import com.ebnbin.eb.getArgument
import com.ebnbin.eb.getArgumentOrDefault
import com.ebnbin.eb.getCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder

open class AlertDialogFragment : AppCompatDialogFragment() {
    protected open val isMaterial: Boolean
        get() = getArgumentOrDefault(KEY_IS_MATERIAL, false)
    protected open val themeId: Int
        get() = getArgumentOrDefault(KEY_THEME_ID, 0)
    protected open val title: CharSequence?
        get() = getArgument(KEY_TITLE)
    protected open val message: CharSequence?
        get() = getArgument(KEY_MESSAGE)
    protected open val positiveText: CharSequence?
        get() = getArgument(KEY_POSITIVE_TEXT)
    protected open val negativeText: CharSequence?
        get() = getArgument(KEY_NEGATIVE_TEXT)
    protected open val neutralText: CharSequence?
        get() = getArgument(KEY_NEUTRAL_TEXT)
    protected open val dialogCancelable: DialogCancelable
        get() = getArgumentOrDefault(KEY_DIALOG_CANCELABLE, DialogCancelable.CANCELABLE)
    protected open val fragmentCallback: FragmentCallback
        get() = getArgumentOrDefault(KEY_FRAGMENT_CALLBACK, FragmentCallback.PREFER_PARENT_FRAGMENT)
    protected open val callbackBundle: Bundle
        get() = getArgumentOrDefault(KEY_CALLBACK_BUNDLE, Bundle.EMPTY)

    //*****************************************************************************************************************

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = if (isMaterial) {
            MaterialAlertDialogBuilder(requireContext(), themeId)
        } else {
            AlertDialog.Builder(requireContext(), themeId)
        }.setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText, null)
            .setNegativeButton(negativeText, null)
            .setNeutralButton(neutralText, null)
        onCreateAlertDialogBuilder(builder)
        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            it as AlertDialog
            it.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener { _ ->
                if (onAlertDialogPositive(it)) dismissAllowingStateLoss()
            }
            it.getButton(AlertDialog.BUTTON_NEGATIVE)?.setOnClickListener { _ ->
                if (onAlertDialogNegative(it)) dismissAllowingStateLoss()
            }
            it.getButton(AlertDialog.BUTTON_NEUTRAL)?.setOnClickListener { _ ->
                if (onAlertDialogNeutral(it)) dismissAllowingStateLoss()
            }
            onAlertDialogShow(it)
        }
        alertDialog.setCanceledOnTouchOutside(dialogCancelable == DialogCancelable.CANCELABLE)
        isCancelable = dialogCancelable != DialogCancelable.NOT_CANCELABLE
        onCreateAlertDialog(alertDialog)
        return alertDialog
    }

    //*****************************************************************************************************************

    protected val alertDialog: AlertDialog?
        get() = dialog as AlertDialog?

    protected fun requireAlertDialog(): AlertDialog {
        return alertDialog ?: throw IllegalStateException("无法获取 AlertDialog.")
    }

    //*****************************************************************************************************************

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onAlertDialogCancel(dialog as AlertDialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onAlertDialogDismiss(dialog as AlertDialog)
    }

    //*****************************************************************************************************************

    protected open fun onCreateAlertDialogBuilder(builder: AlertDialog.Builder) {
        getCallback<Callback>(fragmentCallback)?.onCreateAlertDialogBuilder(builder, callbackBundle)
    }

    protected open fun onCreateAlertDialog(alertDialog: AlertDialog) {
        getCallback<Callback>(fragmentCallback)?.onCreateAlertDialog(alertDialog, callbackBundle)
    }

    protected open fun onAlertDialogPositive(alertDialog: AlertDialog): Boolean {
        return getCallback<Callback>(fragmentCallback)?.onAlertDialogPositive(alertDialog, callbackBundle) != false
    }

    protected open fun onAlertDialogNegative(alertDialog: AlertDialog): Boolean {
        return getCallback<Callback>(fragmentCallback)?.onAlertDialogNegative(alertDialog, callbackBundle) != false
    }

    protected open fun onAlertDialogNeutral(alertDialog: AlertDialog): Boolean {
        return getCallback<Callback>(fragmentCallback)?.onAlertDialogNeutral(alertDialog, callbackBundle) != false
    }

    protected open fun onAlertDialogShow(alertDialog: AlertDialog) {
        getCallback<Callback>(fragmentCallback)?.onAlertDialogShow(alertDialog, callbackBundle)
    }

    protected open fun onAlertDialogCancel(alertDialog: AlertDialog) {
        getCallback<Callback>(fragmentCallback)?.onAlertDialogCancel(alertDialog, callbackBundle)
    }

    protected open fun onAlertDialogDismiss(alertDialog: AlertDialog) {
        getCallback<Callback>(fragmentCallback)?.onAlertDialogDismiss(alertDialog, callbackBundle)
    }

    //*****************************************************************************************************************

    interface Callback {
        fun onCreateAlertDialogBuilder(builder: AlertDialog.Builder, callbackBundle: Bundle) = Unit

        fun onCreateAlertDialog(alertDialog: AlertDialog, callbackBundle: Bundle) = Unit

        /**
         * @return 点击按钮是否 dismiss 对话框.
         */
        fun onAlertDialogPositive(alertDialog: AlertDialog, callbackBundle: Bundle): Boolean = true

        /**
         * @return 点击按钮是否 dismiss 对话框.
         */
        fun onAlertDialogNegative(alertDialog: AlertDialog, callbackBundle: Bundle): Boolean = true

        /**
         * @return 点击按钮是否 dismiss 对话框.
         */
        fun onAlertDialogNeutral(alertDialog: AlertDialog, callbackBundle: Bundle): Boolean = true

        fun onAlertDialogShow(alertDialog: AlertDialog, callbackBundle: Bundle) = Unit

        fun onAlertDialogCancel(alertDialog: AlertDialog, callbackBundle: Bundle) = Unit

        fun onAlertDialogDismiss(alertDialog: AlertDialog, callbackBundle: Bundle) = Unit
    }

    //*****************************************************************************************************************

    companion object {
        const val KEY_IS_MATERIAL: String = "is_material"
        const val KEY_THEME_ID: String = "theme_id"
        const val KEY_TITLE: String = "title"
        const val KEY_MESSAGE: String = "message"
        const val KEY_POSITIVE_TEXT: String = "positive_text"
        const val KEY_NEGATIVE_TEXT: String = "negative_text"
        const val KEY_NEUTRAL_TEXT: String = "neutral_text"
        const val KEY_DIALOG_CANCELABLE: String = "dialog_cancelable"
        const val KEY_FRAGMENT_CALLBACK = "fragment_callback"
        const val KEY_CALLBACK_BUNDLE = "callback_bundle"


        fun createArguments(
            /**
             * 是否使用 [MaterialAlertDialogBuilder].
             */
            isMaterial: Boolean = false,
            themeId: Int = 0,
            title: CharSequence? = null,
            message: CharSequence? = null,
            positiveText: CharSequence? = null,
            negativeText: CharSequence? = null,
            neutralText: CharSequence? = null,
            dialogCancelable: DialogCancelable = DialogCancelable.CANCELABLE,
            fragmentCallback: FragmentCallback = FragmentCallback.PREFER_PARENT_FRAGMENT,
            callbackBundle: Bundle = Bundle.EMPTY
        ): Bundle {
            return bundleOf(
                KEY_IS_MATERIAL to isMaterial,
                KEY_THEME_ID to themeId,
                KEY_TITLE to title,
                KEY_MESSAGE to message,
                KEY_POSITIVE_TEXT to positiveText,
                KEY_NEGATIVE_TEXT to negativeText,
                KEY_NEUTRAL_TEXT to neutralText,
                KEY_DIALOG_CANCELABLE to dialogCancelable,
                KEY_FRAGMENT_CALLBACK to fragmentCallback,
                KEY_CALLBACK_BUNDLE to callbackBundle
            )
        }
    }
}
