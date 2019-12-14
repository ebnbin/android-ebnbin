package com.ebnbin.eb2.sharedpreferences

import androidx.appcompat.app.AppCompatDelegate

/**
 * 偏好管理类.
 */
@Suppress("ClassName")
object EBSpManager {
    internal var version: Sp<Int> = EBSp("version", 0)

    internal var night_mode: Sp<Int> = EBSp("night_mode", AppCompatDelegate.MODE_NIGHT_NO)

    internal var last_update_timestamp: Sp<Long> = EBSp("last_update_timestamp", 0L)

    internal var last_report_timestamp: Sp<Long> = EBSp("last_report_timestamp", 0L)

    internal var debug_page: Sp<Int> = EBSp("debug_page", 0)
}
