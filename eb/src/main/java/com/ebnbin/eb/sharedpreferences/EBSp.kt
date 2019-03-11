package com.ebnbin.eb.sharedpreferences

import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.event.NightModeEvent
import com.ebnbin.eb.library.eventBus
import com.ebnbin.eb.sharedpreferences.delegate.EBDebugSharedPreferencesDelegate
import com.ebnbin.eb.sharedpreferences.delegate.EBSharedPreferencesDelegate

/**
 * 偏好代理帮助类.
 */
@Suppress("ClassName")
object EBSp {
    object eb {
        /**
         * 设置夜间模式. 发送 NightModeEvent 事件.
         */
        var night_mode by EBSharedPreferencesDelegate(
            "night_mode",
            AppCompatDelegate.MODE_NIGHT_NO
        ) { oldValue, newValue ->
            if (oldValue == newValue) return@EBSharedPreferencesDelegate true
            AppCompatDelegate.setDefaultNightMode(newValue)
            eventBus.post(NightModeEvent(oldValue, newValue))
            false
        }
    }

    object eb_debug {
        internal var page by EBDebugSharedPreferencesDelegate("page", 0)
    }
}
