package com.ebnbin.eb.app2.dev

import androidx.annotation.Keep
import com.ebnbin.eb.app2.BuildConfig
import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb.app2.util.DeviceUtil
import com.ebnbin.eb.library.gson

@Keep
class Report private constructor() {
    var deviceId: String? = null
        private set
    var flavor: String? = null
        private set
    var app: Any? = null
        private set

    override fun toString(): String {
        return gson.toJson(this)
    }

    companion object {
        fun create(enableAppReport: Boolean = true): Report {
            return Report().apply {
                deviceId = DeviceUtil.DEVICE_ID
                flavor = BuildConfig.FLAVOR
                if (enableAppReport) {
                    app = EBApp.instance.createAppReport()
                }
            }
        }
    }
}
