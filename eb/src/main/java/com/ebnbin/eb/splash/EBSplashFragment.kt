package com.ebnbin.eb.splash

import android.os.Bundle
import com.ebnbin.eb.app.EBFragment
import com.ebnbin.eb.sharedpreferences.EBSpManager
import com.ebnbin.eb.util.BuildHelper

open class EBSplashFragment : EBFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val oldVersion = EBSpManager.version.value
            val newVersion = BuildHelper.versionCode
            if (oldVersion < newVersion) {
                onNewVersion(oldVersion, newVersion)
                EBSpManager.version.value = newVersion
            }
        }
    }

    protected open fun onNewVersion(oldVersion: Int, newVersion: Int) {
    }
}
