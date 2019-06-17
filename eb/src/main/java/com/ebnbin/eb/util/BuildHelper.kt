package com.ebnbin.eb.util

import android.content.pm.PackageManager
import android.os.Build

object BuildHelper {
    val applicationId: String
        get() = ebApp.packageName

    val simpleApplicationId: String
        get() = applicationId.substringAfter("com.ebnbin.")

    val versionCode: Int
        get() {
            val packageInfo = ebApp.packageManager.getPackageInfo(applicationId, 0)
            return if (sdk28P()) {
                packageInfo.longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode
            }
        }

    val versionName: String
        get() {
            val packageInfo = ebApp.packageManager.getPackageInfo(applicationId, 0)
            return packageInfo.versionName
        }

    val signatureMD5: String
        get() {
            val signature = if (sdk28P()) {
                val signingInfo = ebApp.packageManager
                    .getPackageInfo(applicationId, PackageManager.GET_SIGNING_CERTIFICATES).signingInfo
                if (signingInfo.hasMultipleSigners()) {
                    signingInfo.apkContentsSigners
                } else {
                    signingInfo.signingCertificateHistory
                }
            } else {
                @Suppress("DEPRECATION")
                ebApp.packageManager.getPackageInfo(applicationId, PackageManager.GET_SIGNATURES).signatures
            }[0]
            return DataHelper.md5ToString(signature.toByteArray())
        }

    fun isSignatureValid(): Boolean {
        return signatureMD5 == "f19d826f5e45fdd1389f0b6b9878c263"
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
