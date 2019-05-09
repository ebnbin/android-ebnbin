package com.ebnbin.eb.update

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.R
import com.ebnbin.eb.dialog.EBDialogFragment
import com.ebnbin.eb.githubapi.model.content.Update
import com.ebnbin.eb.util.IntentHelper

internal class UpdateDialogFragment : EBDialogFragment() {
    private lateinit var update: Update

    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        update = arguments.getSerializable("update") as Update
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hasForceUpdate = update.hasForceUpdate()
        isCancelable = !hasForceUpdate
        val titleStringId = if (hasForceUpdate) R.string.eb_update_title_force else R.string.eb_update_title
        val negativeStringId = if (hasForceUpdate) R.string.eb_update_negative_force else R.string.eb_update_negative
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(titleStringId)
            .setMessage(update.message)
            .setPositiveButton(R.string.eb_update_positive, null)
            .setNegativeButton(negativeStringId) { _, _ ->
                if (hasForceUpdate) {
                    IntentHelper.finishApp()
                }
            }
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnShowListener {
            val positiveButton = (it as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                IntentHelper.startBrowser(requireContext(), update.url)
                if (!hasForceUpdate) {
                    dismiss()
                }
            }
        }
        return dialog
    }

    companion object {
        fun start(fm: FragmentManager, update: Update): UpdateDialogFragment {
            val fragment = UpdateDialogFragment()
            fragment.arguments = bundleOf(
                "update" to update
            )
            fragment.show(fm, UpdateDialogFragment::class.java.name)
            return fragment
        }
    }
}
