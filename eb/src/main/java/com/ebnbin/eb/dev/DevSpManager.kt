package com.ebnbin.eb.dev

import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.sharedpreferences.Sp
import com.ebnbin.eb.sharedpreferences.getSharedPreferencesName

internal object DevSpManager {
    val name: String
        get() = EBApplication.instance.getSharedPreferencesName("_eb")

    val floating_x: Sp<Int> = Sp({ EBApplication.instance }, "dev_floating_x", 0, { name })
    val floating_y: Sp<Int> = Sp({ EBApplication.instance }, "dev_floating_y", 0, { name })
    val page: Sp<Int> = Sp({ EBApplication.instance }, "dev_page", 0, { name })
}
