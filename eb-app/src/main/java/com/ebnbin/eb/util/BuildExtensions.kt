package com.ebnbin.eb.util

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import com.ebnbin.eb.context.applicationId

val Context.ebnbinApplicationId: String
    get() = applicationId.substringAfter("com.ebnbin.")

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
