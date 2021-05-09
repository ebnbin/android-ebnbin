package com.ebnbin.eb.report

import android.os.Build
import android.os.Parcelable
import androidx.annotation.Keep
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.androidId
import com.ebnbin.eb.appLabel
import com.ebnbin.eb.applicationId
import com.ebnbin.eb.getDisplayRealSize0
import com.ebnbin.eb.library.gson
import com.ebnbin.eb.locales
import com.ebnbin.eb.md5ToString
import com.ebnbin.eb.signatures
import com.ebnbin.eb.toTimeString
import com.ebnbin.eb.versionCode
import com.ebnbin.eb.versionName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
open class Report(
    val applicationId: String = EBApplication.instance.applicationId,
    val appLabel: String = EBApplication.instance.appLabel,
    val androidId: String = EBApplication.instance.androidId,
    val versionCode: Int = EBApplication.instance.versionCode,
    val versionName: String = EBApplication.instance.versionName,
    val buildType: String = BuildConfig.BUILD_TYPE,
    val buildTimestamp: String = BuildConfig.BUILD_TIMESTAMP.toTimeString(),
    val timestamp: String = com.ebnbin.eb.timestamp().toTimeString(),
    val signatures: List<String> = EBApplication.instance.signatures.map { it.toByteArray().md5ToString() },
    val sdk: Int = Build.VERSION.SDK_INT,
    val brand: String = Build.BRAND.toString(),
    val model: String = Build.MODEL.toString(),
    val abis: List<String> = Build.SUPPORTED_ABIS.toList(),
    val locales: List<String> = EBApplication.instance.locales.map { it.toString() },
    val density: Float = EBApplication.instance.resources.displayMetrics.density,
    val displayRealWidth: Int = EBApplication.instance.getDisplayRealSize0().first,
    val displayRealHeight: Int = EBApplication.instance.getDisplayRealSize0().second
) : Parcelable {
    override fun toString(): String {
        return gson.toJson(this)
    }
}
