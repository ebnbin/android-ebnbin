package com.ebnbin.eb2.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb2.fragment.FragmentHelper
import com.ebnbin.eb2.util.Consts
import java.io.Serializable

class SimpleDialogFragment : EBDialogFragment() {
    private var callback: Callback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = attachCallback(Callback::class.java)
    }

    interface Callback {
        /**
         * @return 是否 dismiss.
         */
        fun onDialogPositive(extraData: Bundle): Boolean

        fun onDialogNegative(extraData: Bundle): Boolean

        fun onDialogNeutral(extraData: Bundle): Boolean

        fun onDialogDismiss(extraData: Bundle)
    }

    private lateinit var builder: Builder
    private lateinit var extraData: Bundle

    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        builder = arguments.getSerializable("builder") as Builder? ?: throw RuntimeException()
        extraData = arguments.getBundle(Consts.KEY_EXTRA_DATA) ?: throw RuntimeException()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = builder.dialogCancel != DialogCancel.NOT_CANCELABLE
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle(builder.title)
            .setMessage(builder.message)
            .setPositiveButton(builder.positive, null)
            .setNegativeButton(builder.negative, null)
            .setNeutralButton(builder.neutral, null)
        val dialog = alertDialogBuilder.create()
        dialog.setOnShowListener {
            val alertDialog = it as AlertDialog
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
                if (callback?.onDialogPositive(extraData) != false) {
                    dismissAllowingStateLoss()
                }
            }
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setOnClickListener {
                if (callback?.onDialogNegative(extraData) != false) {
                    dismissAllowingStateLoss()
                }
            }
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.setOnClickListener {
                if (callback?.onDialogNeutral(extraData) != false) {
                    dismissAllowingStateLoss()
                }
            }
        }
        dialog.setCanceledOnTouchOutside(builder.dialogCancel == DialogCancel.CANCELABLE)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback?.onDialogDismiss(extraData)
    }

    class Builder(
        val title: CharSequence? = null,
        val message: CharSequence? = null,
        val positive: CharSequence? = null,
        val negative: CharSequence? = null,
        val neutral: CharSequence? = null,
        val dialogCancel: DialogCancel = DialogCancel.CANCELABLE
    ) : Serializable

    companion object {
        fun start(
            fm: FragmentManager,
            builder: Builder,
            tag: String,
            extraData: Bundle = Bundle.EMPTY
        ): SimpleDialogFragment {
            return FragmentHelper.add(fm, SimpleDialogFragment::class.java, tag = tag, arguments = bundleOf(
                "builder" to builder,
                Consts.KEY_EXTRA_DATA to extraData
            ))
        }
    }
}
