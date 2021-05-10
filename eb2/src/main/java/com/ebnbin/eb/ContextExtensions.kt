package com.ebnbin.eb

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Surface
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.core.os.postDelayed
import com.ebnbin.eb.activity.openActivity
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt

val Context.applicationId: String
    get() = packageName

val Context.appLabel: String
    get() = applicationInfo.loadLabel(packageManager).toString()

val Context.appIcon: Drawable
    get() = applicationInfo.loadIcon(packageManager)

val Context.appLogo: Drawable?
    get() = applicationInfo.loadLogo(packageManager)

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

val Context.androidId: String
    @SuppressLint("HardwareIds")
    get() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID).toString()

//*********************************************************************************************************************

inline fun <reified T : Any> Context.requireSystemService(): T {
    return getSystemService() ?: throw NullPointerException()
}

//*********************************************************************************************************************

fun Context.dpToPx(dp: Float): Float {
    return dp * resources.displayMetrics.density
}

/**
 * 四舍五入取整.
 */
fun Context.dpToPxRound(dp: Float): Int {
    return dpToPx(dp).roundToInt()
}

/**
 * 向下取整, 误差可能较大.
 */
@Deprecated("Use dpToPxRound instead.", ReplaceWith(""))
fun Context.dpToPxInt(dp: Float): Int {
    return dpToPx(dp).toInt()
}

fun Context.spToPx(sp: Float): Float {
    return sp * resources.displayMetrics.scaledDensity
}

/**
 * 四舍五入取整.
 */
fun Context.spToPxRound(sp: Float): Int {
    return spToPx(sp).roundToInt()
}

/**
 * 向下取整, 误差可能较大.
 */
@Deprecated("Use spToPxRound instead.", ReplaceWith(""))
fun Context.spToPxInt(sp: Float): Int {
    return spToPx(sp).toInt()
}

fun Context.pxToDp(px: Float): Float {
    return px / resources.displayMetrics.density
}

fun Context.pxToDp(px: Int): Float {
    return pxToDp(px.toFloat())
}

fun Context.pxToSp(px: Float): Float {
    return px / resources.displayMetrics.scaledDensity
}

fun Context.pxToSp(px: Int): Float {
    return pxToSp(px.toFloat())
}

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

//*********************************************************************************************************************

@Suppress("DEPRECATION")
inline fun <reified T : Service> Context.isServiceRunning(): Boolean {
    return requireSystemService<ActivityManager>().getRunningServices(Int.MAX_VALUE).any {
        it.service.className == T::class.java.name
    }
}

//*********************************************************************************************************************

fun Context.copy(text: CharSequence, label: CharSequence = applicationId) {
    requireSystemService<ClipboardManager>().setPrimaryClip(ClipData.newPlainText(label, text))
}

//*********************************************************************************************************************

fun Context.getDisplayRealSize(): Triple<Int, Int, Int> {
    val display = requireSystemService<WindowManager>().defaultDisplay
    val outSize = Point().also {
        display.getRealSize(it)
    }
    return Triple(outSize.x, outSize.y, display.rotation)
}

fun Context.getDisplayRealSize0(): Pair<Int, Int> {
    val displayRealSize = getDisplayRealSize()
    return if (displayRealSize.third == Surface.ROTATION_90 || displayRealSize.third == Surface.ROTATION_270) {
        displayRealSize.second to displayRealSize.first
    } else {
        displayRealSize.first to displayRealSize.second
    }
}

//*********************************************************************************************************************

fun Context.getActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun Context.requireActivity(): Activity {
    return getActivity() ?: throw IllegalArgumentException("无法获取 Activity.")
}

//*********************************************************************************************************************

fun Context.closeApp(closeApp: Boolean = true, reopenApp: Boolean = false, killProcessDelay: Long = -1L) {
    if (closeApp) {
        val activity = getActivity()
        if (activity != null) ActivityCompat.finishAffinity(activity)
    }
    mainHandler.postDelayed(max(0L, killProcessDelay)) {
        if (reopenApp) {
            packageManager.getLaunchIntentForPackage(applicationId)
                ?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                ?.let { openActivity(it) }
        }
        if (killProcessDelay >= 0L) {
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }
}

//*********************************************************************************************************************

fun Context.openMarket(
    googlePlayStore: Boolean = false,
    onFailure: ((Throwable) -> Unit)? = { toast(R.string.eb_open_market_failure) }
) {
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${applicationId}"))
        if (googlePlayStore) {
            intent.setPackage("com.android.vending")
        }
        openActivity(intent)
    }.onFailure {
        onFailure?.invoke(it)
    }
}

fun Context.openBrowser(
    url: String,
    chrome: Boolean = false,
    onFailure: ((Throwable) -> Unit)? = { toast(R.string.eb_open_browser_failure) }
) {
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (chrome) {
            intent.setPackage("com.android.chrome")
        }
        openActivity(intent)
    }.onFailure {
        onFailure?.invoke(it)
    }
}

//*********************************************************************************************************************

val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)
