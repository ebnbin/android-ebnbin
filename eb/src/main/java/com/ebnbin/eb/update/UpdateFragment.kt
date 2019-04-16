package com.ebnbin.eb.update

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.app.EBFragment
import com.ebnbin.eb.app.FragmentHelper
import com.ebnbin.eb.net.NetHelper

class UpdateFragment : EBFragment() {
    // TODO
    private var silent: Boolean = false

    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        silent = arguments.getBoolean("silent", false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            asyncRequest(
                NetHelper.ebService.update(),
                {
                    if (it.hasUpdate()) {
                        UpdateDialogFragment.start(childFragmentManager, it)
                    }
                }
            )
        }
    }

    companion object {
        fun start(fm: FragmentManager, silent: Boolean) {
            FragmentHelper.add(fm, UpdateFragment::class.java) {
                putBoolean("silent", silent)
            }
        }
    }
}
