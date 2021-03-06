package com.ebnbin.windowcamera.view

import android.content.Context
import com.ebnbin.windowcamera.profile.enumeration.ProfileToast

interface IWindowCameraViewCallback {
    fun getContext(): Context

    /**
     * 读取 profile 显示 toast.
     */
    fun toast(text: String, long: Boolean = false, profileToast: ProfileToast = ProfileToast.get())
}
