package com.ebnbin.eb.app2.util

import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb.androidId
import com.ebnbin.eb.md5ToString

object DeviceUtil {
    val DEVICE_ID: String = EBApp.instance.androidId.toByteArray().md5ToString()
}
