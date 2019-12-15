package com.ebnbin.sample

import com.ebnbin.eb.EBApp
import com.ebnbin.eb.dev.EBDevFragment
import com.ebnbin.sample.dev.DevFragment

class App : EBApp() {
    override val devFragmentClass: Class<out EBDevFragment> = DevFragment::class.java
}
