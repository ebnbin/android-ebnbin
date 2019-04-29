package com.ebnbin.eb.util

import android.os.Build

object BuildHelper {
    val versionCode: Int
        get() {
            val packageInfo = ebApp.packageManager.getPackageInfo(ebApp.packageName, 0)
            return if (sdk28P()) {
                packageInfo.longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode
            }
        }

    val versionName: String
        get() {
            val packageInfo = ebApp.packageManager.getPackageInfo(ebApp.packageName, 0)
            return packageInfo.versionName
        }

    /**
     * 判断 sdk 版本.
     */
    private fun sdk(versionCode: Int): Boolean = Build.VERSION.SDK_INT >= versionCode

    fun sdk24N(): Boolean = sdk(Build.VERSION_CODES.N)

    fun sdk25N1(): Boolean = sdk(Build.VERSION_CODES.N_MR1)

    fun sdk26O(): Boolean = sdk(Build.VERSION_CODES.O)

    fun sdk27O1(): Boolean = sdk(Build.VERSION_CODES.O_MR1)

    fun sdk28P(): Boolean = sdk(Build.VERSION_CODES.P)
}
