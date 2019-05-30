package com.ebnbin.windowcamera.util

import com.ebnbin.eb.sharedpreferences.Sp
import com.ebnbin.eb.util.res
import com.ebnbin.windowcamera.R

object SpManager {
    val is_night_mode: Sp<Boolean> = Sp(R.string.sp_is_night_mode, res.getBoolean(R.bool.sp_is_night_mode_default_value))
    val is_tip_enabled: Sp<Boolean> = Sp(R.string.sp_is_tip_enabled, res.getBoolean(R.bool.sp_is_tip_enabled_default_value))
}
