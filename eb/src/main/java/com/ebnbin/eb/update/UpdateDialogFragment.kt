package com.ebnbin.eb.update

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
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
        return AlertDialog.Builder(requireContext())
            .setMessage(update.message)
            .setPositiveButton("确定") { _, _ ->
                IntentHelper.startBrowser(requireContext(), update.url)
                if (hasForceUpdate) {
                    IntentHelper.finishApp()
                }
            }
            .setNegativeButton("取消") { _, _ ->
                if (hasForceUpdate) {
                    IntentHelper.finishApp()
                }
            }.create()
    }

    companion object {
        fun start(fm: FragmentManager, update: Update) {
            val fragment = UpdateDialogFragment()
            val arguments = Bundle()
            arguments.putSerializable("update", update)
            fragment.arguments = arguments
            fragment.show(fm, UpdateDialogFragment::class.java.name)
        }
    }
}
