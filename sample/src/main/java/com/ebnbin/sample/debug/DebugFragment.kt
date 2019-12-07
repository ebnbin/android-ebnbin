package com.ebnbin.sample.debug

import com.ebnbin.eb.debug.EBDebugFragment

class DebugFragment : EBDebugFragment() {
    override fun onAddDebugItems() {
        addDebugItem("Debug")
        super.onAddDebugItems()
    }
}
