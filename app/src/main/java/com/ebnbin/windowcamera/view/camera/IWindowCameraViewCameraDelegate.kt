package com.ebnbin.windowcamera.view.camera

import com.ebnbin.windowcamera.view.IWindowCameraViewDelegate

interface IWindowCameraViewCameraDelegate : IWindowCameraViewDelegate {
    /**
     * 打开相机.
     */
    fun openCamera()

    /**
     * 关闭相机.
     */
    fun closeCamera()

    /**
     * 拍摄照片或开始/停止录制视频.
     */
    fun capture()
}
