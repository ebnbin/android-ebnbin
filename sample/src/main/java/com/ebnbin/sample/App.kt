package com.ebnbin.sample

import com.ebnbin.eb.EBApp
import com.ebnbin.eb.dev.DevFragment
import com.ebnbin.sample.dev.AppDevFragment

class App : EBApp() {
    override val devFragmentClass: Class<out DevFragment>
        get() = AppDevFragment::class.java
}
