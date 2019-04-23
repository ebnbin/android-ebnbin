package com.ebnbin.eb.sharedpreferences

import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.event.NightModeEvent
import com.ebnbin.eb.library.eventBus

/**
 * 偏好代理帮助类.
 */
@Suppress("ClassName")
object EBSp {
    object eb {
        /**
         * 设置夜间模式. 发送 NightModeEvent 事件.
         */
        var night_mode: SpItem<Int> = SpItem(
            "night_mode",
            { AppCompatDelegate.MODE_NIGHT_NO },
            { SpHelper.getSharedPreferences("${SpHelper.defaultSharedPreferencesName}_eb") }
        ) { oldValue, newValue ->
            if (oldValue == newValue) return@SpItem true
            AppCompatDelegate.setDefaultNightMode(newValue)
            eventBus.post(NightModeEvent(oldValue, newValue))
            false
        }

        /**
         * 上次请求 update 接口时间.
         */
        internal var request_update_timestamp: SpItem<Long> = EBSpItem("request_update_timestamp", 0L)
    }

    object eb_debug {
        internal var page: EBDebugSpItem<Int> = EBDebugSpItem("page", 0)
    }
}
