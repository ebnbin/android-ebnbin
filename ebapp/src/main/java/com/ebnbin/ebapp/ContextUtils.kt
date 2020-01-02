package com.ebnbin.ebapp

import android.content.Context
import com.ebnbin.eb.androidId
import com.ebnbin.eb.applicationId
import com.ebnbin.eb.md5ToString

val Context.deviceId: String
    get() = androidId.toByteArray().md5ToString()

val Context.ebnbinApplicationId: String
    get() = applicationId.substringAfter("com.ebnbin.")
