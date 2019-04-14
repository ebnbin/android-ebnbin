package com.ebnbin.eb.util

import android.Manifest
import android.app.ActivityManager
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.hardware.camera2.CameraManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Display
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
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

val activityManager: ActivityManager
    get() = ebApp.getSystemService() ?: throw RuntimeException()

val cameraManager: CameraManager
    get() = ebApp.getSystemService() ?: throw RuntimeException()

val notificationManager: NotificationManager
    get() = ebApp.getSystemService() ?: throw RuntimeException()

val vibrator: Vibrator
    get() = ebApp.getSystemService() ?: throw RuntimeException()

val windowManager: WindowManager
    get() = ebApp.getSystemService() ?: throw RuntimeException()

//*********************************************************************************************************************

fun isServiceRunning(serviceClass: Class<out Service>): Boolean {
    @Suppress("DEPRECATION")
    return activityManager.getRunningServices(Int.MAX_VALUE).any {
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

private val display: Display
    get() = windowManager.defaultDisplay

val displayRotation: Int
    get() = display.rotation

val displaySize: RotationSize
    get() {
        val display = display
        val outSize = Point()
        display.getSize(outSize)
        return RotationSize(outSize.x, outSize.y, display.rotation)
    }

val displayRealSize: RotationSize
    get() {
        val display = display
        val outSize = Point()
        display.getRealSize(outSize)
        return RotationSize(outSize.x, outSize.y, display.rotation)
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
        vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(milliseconds)
    }
}

//*********************************************************************************************************************

val versionCode: Int
    get() = ebApp.packageManager.getPackageInfo(ebApp.packageName, 0).longVersionCode.toInt()
