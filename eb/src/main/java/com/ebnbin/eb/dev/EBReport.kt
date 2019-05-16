package com.ebnbin.eb.dev

import android.os.Build
import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.DeviceHelper
import com.ebnbin.eb.util.EBModel
import com.ebnbin.eb.util.ResHelper
import com.ebnbin.eb.util.TimeHelper
import com.ebnbin.eb.util.WindowHelper
import com.ebnbin.eb.util.res

open class EBReport : EBModel {
    var deviceId: String? = null
    var androidId: String? = null
    var version: String? = null
    var timestamp: String? = null
    var sdk: Int? = null
    var manufacturer: String? = null
    var model: String? = null
    var abis: List<String>? = null
    var locales: List<String>? = null
    var density: Float? = null
    var displayRealSize: String? = null
    var displaySize: String? = null
    var displayRealWidthDp: Float? = null
    var displayRealHeightDp: Float? = null

    open fun create(): EBReport {
        deviceId = DeviceHelper.DEVICE_ID
        androidId = DeviceHelper.ANDROID_ID
        version = BuildHelper.versionName
        timestamp = TimeHelper.string("yyyy-MM-dd HH:mm:ss:SSS")
        sdk = Build.VERSION.SDK_INT
        manufacturer = Build.MANUFACTURER
        model = Build.MODEL
        abis = Build.SUPPORTED_ABIS.toList()
        locales = if (BuildHelper.sdk24N()) {
            res.configuration.locales.run {
                (0 until size()).map { get(it).toString() }
            }
        } else {
            @Suppress("DEPRECATION")
            listOf(res.configuration.locale.toString())
        }
        density = ResHelper.density
        displayRealSize = WindowHelper.displayRealSize.run { "${width0}x$height0" }
        displaySize = WindowHelper.displaySize.run { "${width0}x$height0" }
        displayRealWidthDp = WindowHelper.displayRealSize.width0 / ResHelper.density
        displayRealHeightDp = WindowHelper.displayRealSize.height0 / ResHelper.density
        return this
    }

    override fun toString(): String {
        return Libraries.gson.toJson(this)
    }
}
