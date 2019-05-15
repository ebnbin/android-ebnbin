package com.ebnbin.windowcamera.view

import android.content.Context

interface IWindowCameraViewCallback {
    fun getContext(): Context

    /**
     * 读取 profile 显示 toast.
     */
    fun toast(any: Any?, long: Boolean = false)
}
