package com.ebnbin.eb.dev

import android.os.Build
import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.DeviceHelper
import com.ebnbin.eb.util.EBModel
import com.ebnbin.eb.util.ResHelper
import com.ebnbin.eb.util.RotationSize
import com.ebnbin.eb.util.TimeHelper
import com.ebnbin.eb.util.WindowHelper
import com.ebnbin.eb.util.res
import java.util.Arrays

open class EBReport : EBModel {
    val timestamp: String = TimeHelper.string("yyyy-MM-dd HH:mm:ss:SSS")

    val deviceId: String = DeviceHelper.DEVICE_ID

    val androidId: String = DeviceHelper.ANDROID_ID

    val version: String = BuildHelper.versionName

    val manufacturer: String = Build.MANUFACTURER

    val model: String = Build.MODEL

    val sdk: Int = Build.VERSION.SDK_INT

    val displayRealSize: RotationSize = WindowHelper.displayRealSize

    val displaySize: RotationSize = WindowHelper.displaySize

    val density: Float = ResHelper.density

    val displayRealSizeDp: String = WindowHelper.displayRealSize.run { "${width0 / density}x${height0 / density}" }

    val locale: String = if (BuildHelper.sdk24N()) {
        res.configuration.locales.toString()
    } else {
        @Suppress("DEPRECATION")
        res.configuration.locale.toString()
    }

    val abi: String = Arrays.toString(Build.SUPPORTED_ABIS)

    override fun toString(): String {
        return Libraries.gson.toJson(this)
    }
}
