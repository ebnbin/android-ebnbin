package com.ebnbin.windowcamera.view

interface IWindowCameraViewDelegate {
    /**
     * 初始化代理类.
     */
    fun init(windowCameraView: WindowCameraView)

    /**
     * 清理代理类.
     */
    fun dispose()
}
