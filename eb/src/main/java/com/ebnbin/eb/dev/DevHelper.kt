package com.ebnbin.eb.dev

import com.crashlytics.android.Crashlytics
import com.ebnbin.eb.debug.debug
import com.ebnbin.eb.sharedpreferences.EBSpManager
import com.ebnbin.eb.util.DeviceHelper
import com.ebnbin.eb.util.TimeHelper
import com.ebnbin.eb.util.ebApp

object DevHelper {
    private const val DEVICE_EXPIRATION = 7 * 24 * 60 * 60 * 1000L

    fun report(throwable: Throwable) {
        if (!debug) {
            Crashlytics.logException(throwable)
        }
    }

    fun <T : EBReport> report(report: T) {
        if (!TimeHelper.expired(EBSpManager.last_report_timestamp.value, DEVICE_EXPIRATION)) return
        ebApp.asyncHelper.githubPutJson(
            "/report/${DeviceHelper.DEVICE_ID}.json",
            report,
            null,
            onSuccess = {
                EBSpManager.last_report_timestamp.value = TimeHelper.long()
            }
        )
    }
}
