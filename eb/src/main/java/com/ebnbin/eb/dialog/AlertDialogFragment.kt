package com.ebnbin.eb.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.parcel.Parcelize

open class AlertDialogFragment : EBDialogFragment() {
    interface Callback {
        /**
         * @return 点击按钮是否 dismiss.
         */
        fun alertDialogOnPositive(alertDialog: AlertDialog, extraData: Bundle): Boolean = true

        /**
         * @return 点击按钮是否 dismiss.
         */
        fun alertDialogOnNegative(alertDialog: AlertDialog, extraData: Bundle): Boolean = true

        /**
         * @return 点击按钮是否 dismiss.
         */
        fun alertDialogOnNeutral(alertDialog: AlertDialog, extraData: Bundle): Boolean = true

        fun alertDialogOnShow(alertDialog: AlertDialog, extraData: Bundle) = Unit

        fun alertDialogOnCancel(alertDialog: AlertDialog, extraData: Bundle) = Unit

        fun alertDialogOnDismiss(alertDialog: AlertDialog, extraData: Bundle) = Unit
    }

    protected val builder: Builder
        get() = arguments?.getParcelable(KEY_BUILDER) ?: throw RuntimeException()

    @CallSuper
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = builder
        val alertDialogBuilder = if (builder.isMaterial) {
            MaterialAlertDialogBuilder(requireContext())
        } else {
            AlertDialog.Builder(requireContext())
        }.setTitle(builder.title)
            .setMessage(builder.message)
            .setPositiveButton(builder.positiveButtonText, null)
            .setNegativeButton(builder.negativeButtonText, null)
            .setNeutralButton(builder.neutralButtonText, null)
        val dialog = alertDialogBuilder.create()
        dialog.setOnShowListener {
            val alertDialog = it as AlertDialog
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
                if (alertDialogOnPositive(alertDialog)) {
                    dismissAllowingStateLoss()
                }
            }
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setOnClickListener {
                if (alertDialogOnNegative(alertDialog)) {
                    dismissAllowingStateLoss()
                }
            }
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.setOnClickListener {
                if (alertDialogOnNeutral(alertDialog)) {
                    dismissAllowingStateLoss()
                }
            }
            alertDialogOnShow(alertDialog)
        }
        dialog.setCanceledOnTouchOutside(builder.dialogCancelable == DialogCancelable.CANCELABLE)
        isCancelable = builder.dialogCancelable != DialogCancelable.NOT_CANCELABLE
        return dialog
    }

    /**
     * @return 点击按钮是否 dismiss.
     */
    @CallSuper
    protected open fun alertDialogOnPositive(alertDialog: AlertDialog): Boolean {
        return getCallback<Callback>()?.alertDialogOnPositive(alertDialog, builder.extraData) != false
    }

    /**
     * @return 点击按钮是否 dismiss.
     */
    @CallSuper
    protected open fun alertDialogOnNegative(alertDialog: AlertDialog): Boolean {
        return getCallback<Callback>()?.alertDialogOnNegative(alertDialog, builder.extraData) != false
    }

    /**
     * @return 点击按钮是否 dismiss.
     */
    @CallSuper
    protected open fun alertDialogOnNeutral(alertDialog: AlertDialog): Boolean {
        return getCallback<Callback>()?.alertDialogOnNeutral(alertDialog, builder.extraData) != false
    }

    @CallSuper
    protected open fun alertDialogOnShow(alertDialog: AlertDialog) {
        getCallback<Callback>()?.alertDialogOnShow(alertDialog, builder.extraData)
    }

    @CallSuper
    protected open fun alertDialogOnCancel(alertDialog: AlertDialog) {
        getCallback<Callback>()?.alertDialogOnCancel(alertDialog, builder.extraData)
    }

    @CallSuper
    protected open fun alertDialogOnDismiss(alertDialog: AlertDialog) {
        getCallback<Callback>()?.alertDialogOnDismiss(alertDialog, builder.extraData)
    }

    final override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        alertDialogOnCancel(dialog as AlertDialog)
    }

    final override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        alertDialogOnDismiss(dialog as AlertDialog)
    }

    @Parcelize
    class Builder(
        /**
         * 是否使用 MaterialAlertDialogBuilder.
         */
        val isMaterial: Boolean = false,
        val title: CharSequence? = null,
        val message: CharSequence? = null,
        val positiveButtonText: CharSequence? = null,
        val negativeButtonText: CharSequence? = null,
        val neutralButtonText: CharSequence? = null,
        val dialogCancelable: DialogCancelable = DialogCancelable.CANCELABLE,
        /**
         * 回传给 Callback.
         */
        val extraData: Bundle = Bundle.EMPTY
    ) : Parcelable

    companion object {
        const val KEY_BUILDER: String = "builder"
    }
}
