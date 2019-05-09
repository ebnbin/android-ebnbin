package com.ebnbin.eb.splash

import android.os.Bundle
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.sharedpreferences.EBSpManager
import com.ebnbin.eb.util.BuildHelper

open class EBSplashFragment : EBFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val oldVersion = EBSpManager.version.value
            val newVersion = BuildHelper.versionCode
            if (oldVersion < newVersion) {
                EBSpManager.version.value = newVersion
                onNewVersion(oldVersion, newVersion)
            }
        }
    }

    protected open fun onNewVersion(oldVersion: Int, newVersion: Int) {
        EBSpManager.last_update_timestamp.value = 0L
        EBSpManager.last_report_timestamp.value = 0L
    }
}
