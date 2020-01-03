package com.ebnbin.windowcamera.util

import com.ebnbin.eb.EBApplication
import com.ebnbin.eb2.sharedpreferences.Sp
import com.ebnbin.windowcamera.R

object SpManager {
    val is_tip_enabled: Sp<Boolean> = Sp(R.string.sp_is_tip_enabled, EBApplication.instance.resources.getBoolean(R.bool.sp_is_tip_enabled_default_value))
}
