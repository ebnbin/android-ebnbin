package com.ebnbin.eb.sharedpreferences

import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.sharedpreferences.sp.EBDebugSp
import com.ebnbin.eb.sharedpreferences.sp.EBSp
import com.ebnbin.eb.sharedpreferences.sp.Sp

/**
 * 偏好管理类.
 */
@Suppress("ClassName")
object EBSpManager {
    object eb {
        internal var version: Sp<Int> = EBSp("version", 0)

        /**
         * 夜间模式.
         */
        internal var night_mode: Sp<Int> = EBSp("night_mode", AppCompatDelegate.MODE_NIGHT_NO)

        /**
         * 上次请求 update 接口时间.
         */
        internal var request_update_timestamp: Sp<Long> = EBSp("request_update_timestamp", 0L)

        internal var request_device_timestamp: Sp<Long> = EBSp("request_device_timestamp", 0L)
    }

    object eb_debug {
        internal var page: Sp<Int> = EBDebugSp("page", 0)
    }
}
