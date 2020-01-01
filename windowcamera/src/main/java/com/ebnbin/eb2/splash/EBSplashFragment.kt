package com.ebnbin.eb2.splash

import android.os.Bundle
import androidx.annotation.CallSuper
import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb.dialog.AlertDialogFragment
import com.ebnbin.eb.versionCode
import com.ebnbin.eb2.dev.DevHelper
import com.ebnbin.eb2.dev.EBReport
import com.ebnbin.eb2.fragment.EBFragment
import com.ebnbin.eb2.sharedpreferences.EBSpManager

open class EBSplashFragment : EBFragment(), AlertDialogFragment.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DevHelper.report { createReport() }
        onInit(savedInstanceState)
    }

    @CallSuper
    protected open fun onInit(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val oldVersion = EBSpManager.version.value
            val newVersion = EBApp.instance.versionCode
            if (oldVersion < newVersion) {
                EBSpManager.version.value = newVersion
                onNewVersion(oldVersion, newVersion)
            }
        }
    }

    protected open fun createReport(): EBReport {
        return EBReport().create()
    }

    @CallSuper
    protected open fun onNewVersion(oldVersion: Int, newVersion: Int) {
        EBSpManager.last_update_timestamp.value = 0L
        EBSpManager.last_report_timestamp.value = 0L
    }
}
