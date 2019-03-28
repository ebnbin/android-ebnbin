package com.ebnbin.eb.util

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.getSystemService
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

val cameraManager: CameraManager
    get() = ebApp.getSystemService() ?: throw RuntimeException()

val notificationManager: NotificationManager
    get() = ebApp.getSystemService() ?: throw RuntimeException()

val windowManager: WindowManager
    get() = ebApp.getSystemService() ?: throw RuntimeException()
