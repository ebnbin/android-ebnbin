package com.ebnbin.eb

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.core.content.getSystemService
import java.util.Locale
import kotlin.math.roundToInt

val Context.applicationId: String
    get() = packageName

val Context.versionCode: Int
    get() {
        val packageInfo = packageManager.getPackageInfo(applicationId, 0)
        return if (sdk28P()) {
            packageInfo.longVersionCode.toInt()
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode
        }
    }

val Context.versionName: String
    get() = packageManager.getPackageInfo(applicationId, 0).versionName

/**
 * Apk 签名列表.
 */
val Context.signatures: List<Signature>
    get() = if (sdk28P()) {
        val signingInfo = packageManager.getPackageInfo(applicationId, PackageManager.GET_SIGNING_CERTIFICATES)
            .signingInfo
        if (signingInfo.hasMultipleSigners()) {
            signingInfo.apkContentsSigners
        } else {
            signingInfo.signingCertificateHistory
        }
    } else {
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo(applicationId, PackageManager.GET_SIGNATURES).signatures
    }.toList()

//*********************************************************************************************************************

inline fun <reified T : Any> Context.requireSystemService(): T = getSystemService() ?: throw NullPointerException()

//*********************************************************************************************************************

fun Context.dpToPx(dp: Float): Float = dp * resources.displayMetrics.density

/**
 * 四舍五入取整.
 */
fun Context.dpToPxRound(dp: Float): Int = dpToPx(dp).roundToInt()

/**
 * 向下取整, 误差可能较大.
 */
@Deprecated("Use dpToPxRound instead.", ReplaceWith(""))
fun Context.dpToPxInt(dp: Float): Int = dpToPx(dp).toInt()

fun Context.spToPx(sp: Float): Float = sp * resources.displayMetrics.scaledDensity

/**
 * 四舍五入取整.
 */
fun Context.spToPxRound(sp: Float): Int = spToPx(sp).roundToInt()

/**
 * 向下取整, 误差可能较大.
 */
@Deprecated("Use spToPxRound instead.", ReplaceWith(""))
fun Context.spToPxInt(sp: Float): Int = spToPx(sp).toInt()

fun Context.pxToDp(px: Float): Float = px / resources.displayMetrics.density

fun Context.pxToDp(px: Int): Float = pxToDp(px.toFloat())

fun Context.pxToSp(px: Float): Float = px / resources.displayMetrics.scaledDensity

fun Context.pxToSp(px: Int): Float = pxToSp(px.toFloat())

//*********************************************************************************************************************

val Context.locales: List<Locale>
    get() = if (sdk24N()) {
        resources.configuration.locales.let { locales ->
            (0 until locales.size()).map { locales.get(it) }
        }
    } else {
        @Suppress("DEPRECATION")
        listOf(resources.configuration.locale)
    }
