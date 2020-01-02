package com.ebnbin.eb2.dev

import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb2.async.AsyncHelper
import com.ebnbin.eb2.sharedpreferences.EBSpManager
import com.ebnbin.eb2.util.TimeHelper
import com.ebnbin.ebapp.deviceId
import com.ebnbin.ebapp.library.firebaseAnalytics
import com.ebnbin.windowcamera.BuildConfig

object DevHelper {
    private const val REPORT_EXPIRATION = 24 * 60 * 60 * 1000L

    fun reportThrowable(throwable: Throwable) {
        if (BuildConfig.DEBUG) return
        Crashlytics.logException(throwable)
    }

    fun <T : EBReport> report(report: () -> T) {
        if (BuildConfig.DEBUG) return
        if (!TimeHelper.expired(EBSpManager.last_report_timestamp.value, REPORT_EXPIRATION)) return
        AsyncHelper.global.githubPutJson(
            "/reports/${EBApp.instance.deviceId.substring(0, 2)}/${EBApp.instance.deviceId}.json",
            report(),
            null,
            onSuccess = {
                EBSpManager.last_report_timestamp.value = TimeHelper.long()
            }
        )
    }

    fun reportEvent(name: String, params: Bundle = Bundle.EMPTY) {
        if (BuildConfig.DEBUG) return
        firebaseAnalytics.logEvent(name, params)
    }
}
