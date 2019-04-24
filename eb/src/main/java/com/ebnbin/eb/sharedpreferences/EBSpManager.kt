package com.ebnbin.eb.sharedpreferences

import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.event.NightModeEvent
import com.ebnbin.eb.library.eventBus
import com.ebnbin.eb.sharedpreferences.sp.EBDebugSp
import com.ebnbin.eb.sharedpreferences.sp.EBSp
import com.ebnbin.eb.sharedpreferences.sp.Sp

/**
 * 偏好管理类.
 */
@Suppress("ClassName")
object EBSpManager {
    object eb {
        /**
         * 设置夜间模式. 发送 NightModeEvent 事件.
         */
        var night_mode: Sp<Int> = Sp(
            "night_mode",
            { AppCompatDelegate.MODE_NIGHT_NO },
            { SharedPreferencesHelper.getSharedPreferences("_eb") }
        ) { oldValue, newValue ->
            if (oldValue == newValue) return@Sp true
            AppCompatDelegate.setDefaultNightMode(newValue)
            eventBus.post(NightModeEvent(oldValue, newValue))
            false
        }

        /**
         * 上次请求 update 接口时间.
         */
        internal var request_update_timestamp: Sp<Long> = EBSp("request_update_timestamp", 0L)
    }

    object eb_debug {
        internal var page: Sp<Int> = EBDebugSp("page", 0)
    }
}