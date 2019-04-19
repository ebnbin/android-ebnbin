package com.ebnbin.eb.util

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.VibrationEffect
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.RequiresPermission
import androidx.core.content.res.getColorOrThrow
import com.ebnbin.eb.app.EBApplication
import kotlin.math.roundToInt

/**
 * EBApplication 单例.
 */
val ebApp: EBApplication
    get() = EBApplication.instance

//*********************************************************************************************************************

/**
 * 关闭所有 Activity 并重启 MainActivity.
 */
fun restartMainActivity() {
    val intent = ebApp.packageManager.getLaunchIntentForPackage(ebApp.packageName) ?: return
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    ebApp.startActivity(intent)
}

fun closeApp() {
    val intent = ebApp.packageManager.getLaunchIntentForPackage(ebApp.packageName) ?: return
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    intent.putExtra("finish", true)
    ebApp.startActivity(intent)
}

//*********************************************************************************************************************

val Float.dpToPx: Float
    get() = this * ebApp.resources.displayMetrics.density

val Float.dpToPxRound: Int
    get() = dpToPx.roundToInt()

val Float.dpToPxInt: Int
    get() = dpToPx.toInt()

//*********************************************************************************************************************

/**
 * @param any 如果为 Int 则取字符串资源.
 */
fun toast(context: Context, any: Any?, long: Boolean = false) {
    val duration = if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    if (any is Int) {
        Toast.makeText(context, any, duration).show()
    } else {
        Toast.makeText(context, any.toString(), duration).show()
    }
}

//*********************************************************************************************************************

fun isServiceRunning(serviceClass: Class<out Service>): Boolean {
    @Suppress("DEPRECATION")
    return SystemServices.activityManager.getRunningServices(Int.MAX_VALUE).any {
        it.service.className == serviceClass.name
    }
}

//*********************************************************************************************************************

@ColorInt
fun getColorAttr(context: Context, @AttrRes attrId: Int): Int {
    val attrs = intArrayOf(attrId)
    val typedArray = context.obtainStyledAttributes(attrs)
    val index = 0
    val color = typedArray.getColorOrThrow(index)
    typedArray.recycle()
    return color
}

//*********************************************************************************************************************

/**
 * 最大公约数.
 */
tailrec infix fun Int.gcd(other: Int): Int {
    if (this <= 0 || other <= 0) throw RuntimeException()
    return if (this % other == 0) other else other gcd this % other
}

//*********************************************************************************************************************

@RequiresPermission(Manifest.permission.VIBRATE)
fun vibrate(milliseconds: Long) {
    if (sdk26O()) {
        SystemServices.vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        SystemServices.vibrator.vibrate(milliseconds)
    }
}

//*********************************************************************************************************************

val versionCode: Int
    get() = ebApp.packageManager.getPackageInfo(ebApp.packageName, 0).longVersionCode.toInt()
