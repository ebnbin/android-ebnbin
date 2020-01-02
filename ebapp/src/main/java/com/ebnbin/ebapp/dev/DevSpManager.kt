package com.ebnbin.ebapp.dev

import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.sharedpreferences.getSharedPreferencesName

internal object DevSpManager {
    val name: String
        get() = EBApplication.instance.getSharedPreferencesName("_ebapp")
}
