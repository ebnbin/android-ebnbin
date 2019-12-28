package com.ebnbin.sample

import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb.app2.dev.DevFragment2
import com.ebnbin.sample.dev.AppDevFragment

class App : EBApp() {
    override val devFragmentClass: Class<out DevFragment2>
        get() = AppDevFragment::class.java
}
