package com.ebnbin.windowcamera.menu

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.ebnbin.eb.dialog.EBDialogFragment
import com.ebnbin.eb2.fragment.FragmentHelper
import com.ebnbin.windowcamera.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jeremyliao.liveeventbus.LiveEventBus

class MenuFragment : EBDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LiveEventBus.get("MenuDismissEvent").observe(this, Observer {
            dismissAllowingStateLoss()
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        try {
            dialog.setContentView(R.layout.menu_fragment)
        } catch (e: IllegalArgumentException) {
            // 发生在 MenuFragment 还没关闭再次打开导致 MenuPreferenceFragment 被重复添加.
            dismissAllowingStateLoss()
            return dialog
        }
        dialog.behavior.skipCollapsed = true
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        parentFragment?.fragmentManager?.let {
            FragmentHelper.remove(it, MenuPreferenceFragment::class.java.name)
        }
        super.onDismiss(dialog)
    }

    companion object {
        fun start(fm: FragmentManager): MenuFragment {
            return FragmentHelper.add(fm, MenuFragment::class.java)
        }
    }
}
