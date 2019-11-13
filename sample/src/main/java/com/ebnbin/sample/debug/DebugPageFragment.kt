package com.ebnbin.sample.debug

import android.os.Bundle
import android.view.View
import com.ebnbin.eb.debug.BaseDebugPageFragment

/**
 * Debug page 页面.
 */
class DebugPageFragment : BaseDebugPageFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDebugItem("Coroutines") {
            asyncHelper.coroutines()
        }
    }
}
