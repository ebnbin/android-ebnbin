package com.ebnbin.eb.update

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.app.EBDialogFragment
import com.ebnbin.eb.net.model.eb.Update
import com.ebnbin.eb.util.AppHelper

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
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(update.url))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                if (hasForceUpdate) {
                    AppHelper.restartMainActivity(true)
                }
            }
            .setNegativeButton("取消") { _, _ ->
                if (hasForceUpdate) {
                    AppHelper.restartMainActivity(true)
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
