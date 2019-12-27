package com.ebnbin.eb.dev

import com.ebnbin.eb.Sp
import com.ebnbin.eb.getSharedPreferencesName

internal object DevSpManager {
    private val NAME = EBDev.app.getSharedPreferencesName("_eb_dev")

    val floating_x: Sp<Int> = Sp(EBDev.app, "floating_x", 0, NAME)
    val floating_y: Sp<Int> = Sp(EBDev.app, "floating_y", 0, NAME)
}
