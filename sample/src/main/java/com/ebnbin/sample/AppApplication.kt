package com.ebnbin.sample

import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.debug.EBDebugFragment
import com.ebnbin.sample.debug.DebugFragment

class AppApplication : EBApplication() {
    override val debugFragmentClass: Class<out EBDebugFragment> = DebugFragment::class.java
}
