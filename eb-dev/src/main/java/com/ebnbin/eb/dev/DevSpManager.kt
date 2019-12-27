package com.ebnbin.eb.dev

import com.ebnbin.eb.Sp
import com.ebnbin.eb.getSharedPreferencesName

internal object DevSpManager {
    val name: String = EBDev.app.getSharedPreferencesName("_eb_dev")

    val floating_x: Sp<Int> = Sp(EBDev.app, "floating_x", 0, name)
    val floating_y: Sp<Int> = Sp(EBDev.app, "floating_y", 0, name)
}
