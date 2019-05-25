package com.ebnbin.windowcamera.menu

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.dialog.EBDialogFragment
import com.ebnbin.eb.fragment.FragmentHelper
import com.ebnbin.windowcamera.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MenuFragment : EBDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.menu_fragment)
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

    override val isEventBusEnabled: Boolean = true

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MenuDismissEvent) {
        dismissAllowingStateLoss()
    }

    companion object {
        fun start(fm: FragmentManager): MenuFragment {
            return FragmentHelper.add(fm, MenuFragment::class.java)
        }
    }
}