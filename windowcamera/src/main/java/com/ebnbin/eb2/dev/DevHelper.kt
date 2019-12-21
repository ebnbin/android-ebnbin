package com.ebnbin.eb2.dev

import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb.util.DeviceUtil
import com.ebnbin.eb2.async.AsyncHelper
import com.ebnbin.eb2.debug.debug
import com.ebnbin.eb2.sharedpreferences.EBSpManager
import com.ebnbin.eb2.util.TimeHelper

object DevHelper {
    private const val REPORT_EXPIRATION = 24 * 60 * 60 * 1000L

    fun reportThrowable(throwable: Throwable) {
        if (debug) return
        Crashlytics.logException(throwable)
    }

    fun <T : EBReport> report(report: () -> T) {
        if (debug) return
        if (!TimeHelper.expired(EBSpManager.last_report_timestamp.value, REPORT_EXPIRATION)) return
        AsyncHelper.global.githubPutJson(
            "/reports/${DeviceUtil.DEVICE_ID.substring(0, 2)}/${DeviceUtil.DEVICE_ID}.json",
            report(),
            null,
            onSuccess = {
                EBSpManager.last_report_timestamp.value = TimeHelper.long()
            }
        )
    }

    fun reportEvent(name: String, params: Bundle = Bundle.EMPTY) {
        if (debug) return
        Libraries.firebaseAnalytics.logEvent(name, params)
    }
}
