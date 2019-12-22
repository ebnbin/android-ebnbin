package com.ebnbin.eb.context

import android.content.Context
import androidx.core.content.getSystemService
import com.ebnbin.eb.util.sdk28P

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

//*********************************************************************************************************************

inline fun <reified T : Any> Context.requireSystemService(): T = getSystemService() ?: throw NullPointerException()
