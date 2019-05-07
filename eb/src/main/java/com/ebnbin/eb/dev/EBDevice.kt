package com.ebnbin.eb.dev

import android.os.Build
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.DeviceHelper
import com.ebnbin.eb.util.EBModel
import com.ebnbin.eb.util.ResHelper
import com.ebnbin.eb.util.TimeHelper
import com.ebnbin.eb.util.WindowHelper

open class EBDevice : EBModel {
    val timestamp: String = TimeHelper.string("yyyy-MM-dd HH:mm:ss:SSS")

    val deviceId: String = DeviceHelper.DEVICE_ID

    val androidId: String = DeviceHelper.ANDROID_ID

    val version: String = BuildHelper.versionName

    val manufacturer: String = Build.MANUFACTURER

    val model: String = Build.MODEL

    val sdk: Int = Build.VERSION.SDK_INT

    val release: String = Build.VERSION.RELEASE

    val displayRealSize: String = WindowHelper.displayRealSize.run { "${width0}x$height0" }

    val displaySize: String = WindowHelper.displaySize.run { "${width0}x$height0" }

    val density: Float = ResHelper.density

    val displayRealSizeDp: String = WindowHelper.displayRealSize.run { "${width0 / density}x${height0 / density}" }
}
