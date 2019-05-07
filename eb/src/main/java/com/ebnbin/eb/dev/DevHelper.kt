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

    fun <T : EBDevice> device(device: T) {
        if (!TimeHelper.expired(EBSpManager.eb.request_device_timestamp.value, DEVICE_EXPIRATION)) return
        ebApp.asyncHelper.githubPutJson(
            "/devices/${DeviceHelper.DEVICE_ID}.json",
            device,
            null,
            onSuccess = {
                EBSpManager.eb.request_device_timestamp.value = TimeHelper.long()
            }
        )
    }
}
