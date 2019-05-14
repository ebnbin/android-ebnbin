package com.ebnbin.windowcamera.view

import android.content.Context
import android.widget.Toast

interface IWindowCameraViewCallback {
    fun getContext(): Context

    /**
     * Toast.
     *
     * @param duration 如果为 system 样式, 只能是 Toast.LENGTH_SHORT 或 Toast.LENGTH_LONG. 如果为 system_alert_window 样式,
     * 需要权限, Toast.LENGTH_SHORT 显示 3000L 毫秒, Toast.LENGTH_LONG 显示 6000L 毫秒, 其他值显示自定义毫秒.
     *
     * @param forceSystemAlertWindow 读取 profile sp 或者强制 system_alert_window 样式.
     */
    fun windowToast(any: Any?, duration: Int = Toast.LENGTH_SHORT, forceSystemAlertWindow: Boolean = false)
}
