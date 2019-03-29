package com.ebnbin.windowcamera.camera

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraMetadata
import com.ebnbin.eb.util.cameraManager

/**
 * 相机帮助类.
 */
object CameraHelper {
    private val ids: List<String> = cameraManager.cameraIdList.toList()

    /**
     * 对所有摄像头进行检测, 但只使用第一个后置摄像头和第一个前置摄像头, 且后置摄像头和前置摄像头必须存在.
     */
    private val devices: List<Device> = ArrayList<Device>().apply {
        ids.forEachIndexed { index, id ->
            val device = Device(id, index)
            add(device)
        }
    }

    val backDevice: Device
    val frontDevice: Device

    init {
        var backDevice: Device? = null
        var frontDevice: Device? = null
        devices.forEach {
            if (it.isExternal) return@forEach
            if (it.isFront) {
                if (frontDevice == null) frontDevice = it
            } else {
                if (backDevice == null) backDevice = it
            }
        }
        this.backDevice = backDevice ?: throw RuntimeException("没有后置摄像头.")
        this.frontDevice = frontDevice ?: throw RuntimeException("没有前置摄像头.")
    }

    class Device(val id: String, oldId: Int) {
        private val cameraCharacteristics: CameraCharacteristics = cameraManager.getCameraCharacteristics(id)

        private val lensFacing: Int = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) as Int

        val isExternal: Boolean = lensFacing != CameraMetadata.LENS_FACING_FRONT &&
                lensFacing != CameraMetadata.LENS_FACING_BACK

        /**
         * 后置摄像头和外置摄像头都为 false.
         */
        val isFront: Boolean = lensFacing == CameraMetadata.LENS_FACING_FRONT
    }
}
