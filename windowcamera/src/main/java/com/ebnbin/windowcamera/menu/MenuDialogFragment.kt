package com.ebnbin.windowcamera.menu

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.app.EBDialogFragment
import com.ebnbin.windowcamera.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class MenuDialogFragment : EBDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setContentView(R.layout.menu_dialog_fragment)
        dialog.behavior.skipCollapsed = true
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    companion object {
        fun start(fm: FragmentManager) {
            val fragment = MenuDialogFragment()
            fragment.show(fm, MenuDialogFragment::class.java.name)
        }
    }
}
