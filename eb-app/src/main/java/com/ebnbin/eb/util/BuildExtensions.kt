package com.ebnbin.eb.util

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature

val Context.applicationId: String
    get() = packageName

val Context.ebnbinApplicationId: String
    get() = applicationId.substringAfter("com.ebnbin.")

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
