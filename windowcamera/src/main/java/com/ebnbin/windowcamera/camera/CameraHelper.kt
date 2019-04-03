package com.ebnbin.windowcamera.camera

import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.MediaRecorder
import android.util.Size
import com.ebnbin.eb.util.RotationSize
import com.ebnbin.eb.util.cameraManager
import com.ebnbin.eb.util.displayRealSize

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
            try {
                val device = Device(id, index)
                add(device)
            } catch (e: Exception) {
            }
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

        private val sensorOrientation: Int = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) as Int

        private val scalerStreamConfigurationMap: StreamConfigurationMap = cameraCharacteristics.get(
            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) as StreamConfigurationMap

        private val jpegSizes: Array<Size>? = scalerStreamConfigurationMap.getOutputSizes(ImageFormat.JPEG)

        private val surfaceTextureSizes: Array<Size>? = scalerStreamConfigurationMap.getOutputSizes(
            SurfaceTexture::class.java)

        private val mediaRecorderSizes: Array<Size>? = scalerStreamConfigurationMap.getOutputSizes(
            MediaRecorder::class.java)

        val photoResolutions: List<Resolution> = kotlin.run {
            val result = ArrayList<Resolution>()
            jpegSizes
                ?.filter { it.width > 0 && it.height > 0 }
                ?.map { Resolution(it.width, it.height, sensorOrientation) }
                ?.forEach { result.add(it) }
            if (result.isEmpty()) throw RuntimeException("没有有效的照片分辨率.")
            result.sortedDescending()
        }

        private val maxResolution: Resolution = photoResolutions.first()

        val previewResolutions: List<Resolution> = kotlin.run {
            val displayRealSize = displayRealSize
            val result = ArrayList<Resolution>()
            surfaceTextureSizes
                ?.filter { it.width > 0 && it.height > 0 }
                ?.map { Resolution(it.width, it.height, sensorOrientation) }
                ?.filter { it.isRatioEquals(maxResolution) }
                ?.sorted()
                ?.run outer@{
                    forEach {
                        result.add(it)
                        if (it.isWidthHeightGreaterOrEquals(displayRealSize)) return@outer
                    }
                }
            if (result.isEmpty()) throw RuntimeException("没有有效的预览分辨率.")
            result.sortedDescending()
        }

        open class Resolution(width: Int, height: Int, sensorOrientation: Int) :
            RotationSize(width, height, sensorOrientation / 90) {
            override fun toString(): String {
                return "${width}x$height"
            }
        }
    }
}
