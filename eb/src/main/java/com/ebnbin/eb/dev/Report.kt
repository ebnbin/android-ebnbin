package com.ebnbin.eb.dev

import android.os.Build
import androidx.annotation.Keep
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.EBApp
import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb.time.timestamp
import com.ebnbin.eb.time.toTimeString
import com.ebnbin.eb.util.DeviceUtil
import com.ebnbin.eb.util.applicationId
import com.ebnbin.eb.util.locales
import com.ebnbin.eb.util.signatures
import com.ebnbin.eb.util.toMD5String
import com.ebnbin.eb.util.versionCode
import com.ebnbin.eb.util.versionName

@Keep
class Report private constructor() {
    var applicationId: String? = null
        private set
    var deviceId: String? = null
        private set
    var androidId: String? = null
        private set
    var versionCode: Int? = null
        private set
    var versionName: String? = null
        private set
    var flavor: String? = null
        private set
    var buildType: String? = null
        private set
    var buildTimestamp: String? = null
        private set
    var timestamp: String? = null
        private set
    var signatures: List<String?>? = null
        private set
    var sdk: Int? = null
        private set
    var brand: String? = null
        private set
    var model: String? = null
        private set
    var abis: List<String?>? = null
        private set
    var locales: List<String?>? = null
        private set
    var density: Float? = null
        private set

    override fun toString(): String {
        return Libraries.gson.toJson(this)
    }

    companion object {
        fun create(): Report {
            return Report().apply {
                applicationId = EBApp.instance.applicationId
                deviceId = DeviceUtil.DEVICE_ID
                androidId = DeviceUtil.ANDROID_ID
                versionCode = EBApp.instance.versionCode
                versionName = EBApp.instance.versionName
                flavor = BuildConfig.FLAVOR
                buildType = BuildConfig.BUILD_TYPE
                buildTimestamp = BuildConfig.BUILD_TIMESTAMP.toTimeString("yyyy-MM-dd HH:mm:ss:SSS")
                timestamp = timestamp().toTimeString("yyyy-MM-dd HH:mm:ss:SSS")
                signatures = EBApp.instance.signatures.map { it.toByteArray().toMD5String() }
                sdk = Build.VERSION.SDK_INT
                brand = Build.BRAND
                model = Build.MODEL
                abis = Build.SUPPORTED_ABIS.toList()
                locales = EBApp.instance.locales.map { it.toString() }
                density = EBApp.instance.resources.displayMetrics.density
            }
        }
    }
}
