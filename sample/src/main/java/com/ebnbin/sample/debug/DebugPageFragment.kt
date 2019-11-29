package com.ebnbin.sample.debug

import android.os.Bundle
import android.view.View
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.sample.sample.SampleFragment

/**
 * Debug page 页面.
 */
class DebugPageFragment : BaseDebugPageFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDebugItem("SampleFragment") {
            IntentHelper.startFragmentFromFragment(this, SampleFragment::class.java)
        }
    }
}
