package com.ebnbin.eb.util

import android.content.Context
import com.ebnbin.eb.applicationId

val Context.ebnbinApplicationId: String
    get() = applicationId.substringAfter("com.ebnbin.")
