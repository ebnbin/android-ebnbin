package com.ebnbin.ebapp.dev

import com.ebnbin.eb.sharedpreferences.getSharedPreferencesName
import com.ebnbin.ebapp.EBAppApplication

internal object DevSpManager {
    val name: String
        get() = EBAppApplication.instance.getSharedPreferencesName("_ebapp")
}
