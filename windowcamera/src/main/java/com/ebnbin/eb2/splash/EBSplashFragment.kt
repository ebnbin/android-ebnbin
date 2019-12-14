package com.ebnbin.eb2.splash

import android.os.Bundle
import androidx.annotation.CallSuper
import com.ebnbin.eb2.dev.DevHelper
import com.ebnbin.eb2.dev.EBReport
import com.ebnbin.eb2.dialog.SimpleDialogFragment
import com.ebnbin.eb2.fragment.EBFragment
import com.ebnbin.eb2.sharedpreferences.EBSpManager
import com.ebnbin.eb2.util.BuildHelper

open class EBSplashFragment : EBFragment(), SimpleDialogFragment.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DevHelper.report { createReport() }
        onInit(savedInstanceState)
    }

    @CallSuper
    protected open fun onInit(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val oldVersion = EBSpManager.version.value
            val newVersion = BuildHelper.versionCode
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

    @CallSuper
    override fun onDialogPositive(extraData: Bundle): Boolean {
        return true
    }

    @CallSuper
    override fun onDialogNegative(extraData: Bundle): Boolean {
        return true
    }

    @CallSuper
    override fun onDialogNeutral(extraData: Bundle): Boolean {
        return true
    }

    @CallSuper
    override fun onDialogDismiss(extraData: Bundle) {
    }
}
