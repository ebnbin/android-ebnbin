@file:Suppress("DEPRECATION")

package com.ebnbin.windowcamera.camera

import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.util.Size
import android.view.Surface
import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb.requireSystemService
import com.ebnbin.eb2.dev.DevHelper
import com.ebnbin.eb2.util.RotationSize
import com.ebnbin.eb2.util.WindowHelper
import com.ebnbin.windowcamera.dev.Report

/**
 * 相机帮助类.
 *
 * 在使用前必须检测有效性.
 */
object CameraHelper {
    private val CAMCORDER_PROFILE_QUALITIES = listOf(
        CamcorderProfile.QUALITY_2160P,
        CamcorderProfile.QUALITY_1080P,
        CamcorderProfile.QUALITY_720P,
        CamcorderProfile.QUALITY_480P,
        CamcorderProfile.QUALITY_CIF,
        CamcorderProfile.QUALITY_QVGA,
        CamcorderProfile.QUALITY_QCIF,
        CamcorderProfile.QUALITY_HIGH,
        CamcorderProfile.QUALITY_LOW
    )

    private val ids: List<String> = EBApp.instance.requireSystemService<CameraManager>().cameraIdList.toList()

    /**
     * 对所有摄像头进行检测, 但只使用第一个后置摄像头和第一个前置摄像头, 且后置摄像头和前置摄像头都必须存在.
     */
    private val devices: List<Device> = ids
        .mapIndexed { index, id ->
            try {
                Device(id, index)
            } catch (throwable: Throwable) {
                DevHelper.reportThrowable(throwable)
                null
            }
        }
        .filterNotNull()

    private var backDevice: Device? = null
    private var frontDevice: Device? = null

    init {
        devices.forEach {
            if (!it.isValid()) return@forEach
            if (it.isFront) {
                if (frontDevice == null) {
                    frontDevice = it
                }
            } else {
                if (backDevice == null) {
                    backDevice = it
                }
            }
        }
    }

    fun requireBackDevice(): Device {
        return backDevice ?: throw RuntimeException()
    }

    fun requireFrontDevice(): Device {
        return frontDevice ?: throw RuntimeException()
    }

    //*****************************************************************************************************************

    fun isValid(): Boolean {
        return backDevice != null && frontDevice != null
    }

    fun getInvalidString(): String {
        if (isValid()) return "Valid"
        return devices.joinToString(",") { it.getInvalidString() }
    }

    //*****************************************************************************************************************

    fun report(): Report.Camera {
        val camera = Report.Camera()
        camera.ids = ids
        camera.devices = devices.map { it.report() }
        camera.backDevice = backDevice?.id
        camera.frontDevice = frontDevice?.id
        return camera
    }

    //*****************************************************************************************************************

    /**
     * 摄像头.
     *
     * @param id Camera2 API id.
     *
     * @param oldId Camera API id. 不能保证有效.
     */
    class Device(val id: String, private val oldId: Int) {
        private val cameraCharacteristics: CameraCharacteristics =
            EBApp.instance.requireSystemService<CameraManager>().getCameraCharacteristics(id)

        //*************************************************************************************************************

        private val lensFacing: Int = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) as Int

        /**
         * 外置摄像头.
         */
        private val isExternal: Boolean = lensFacing != CameraMetadata.LENS_FACING_FRONT &&
                lensFacing != CameraMetadata.LENS_FACING_BACK

        /**
         * 后置摄像头和外置摄像头都为 false.
         */
        val isFront: Boolean = lensFacing == CameraMetadata.LENS_FACING_FRONT

        //*************************************************************************************************************

        private val sensorOrientation: Int = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) as Int

        /**
         * 根据当前屏幕旋转方向设置拍摄照片或录制视频的方向.
         */
        val sensorOrientations: IntArray = arrayOf(
            Surface.ROTATION_0,
            Surface.ROTATION_90,
            Surface.ROTATION_180,
            Surface.ROTATION_270
        ).map {
            (sensorOrientation + (if (isFront) 90 else -90) * it + 360) % 360
        }.toIntArray()

        //*************************************************************************************************************

        private val sensorInfoPixelArraySize: Size =
            cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE) as Size

        val sensorResolution: Resolution =
            Resolution(sensorInfoPixelArraySize.width, sensorInfoPixelArraySize.height, sensorOrientation)

        //*************************************************************************************************************

        private val scalerStreamConfigurationMap: StreamConfigurationMap =
            cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) as StreamConfigurationMap

        //*************************************************************************************************************

        private val surfaceTextureSizes: Array<Size>? =
            scalerStreamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)

        /**
         * 宽高不都大于屏幕宽高.
         */
        private val previewNotGreaterResolutions: List<Resolution> = run {
            val displayRealSize = WindowHelper.displayRealSize
            (surfaceTextureSizes ?: emptyArray())
                .filter { it.width > 0 && it.height > 0 }
                .map { Resolution(it.width, it.height, sensorOrientation) }
                .toSet()
                .filter { !it.isWidthHeightGreater(displayRealSize) }
                .sortedDescending()
        }

        /**
         * 宽高都小等于屏幕宽高.
         */
        private val previewLessOrEqualsResolutions: List<Resolution> = run {
            val displayRealSize = WindowHelper.displayRealSize
            (surfaceTextureSizes ?: emptyArray())
                .filter { it.width > 0 && it.height > 0 }
                .map { Resolution(it.width, it.height, sensorOrientation) }
                .toSet()
                .filter { it.isWidthHeightLessOrEquals(displayRealSize) }
                .sortedDescending()
        }

        private val defaultPreviewResolution: Resolution? = if (previewLessOrEqualsResolutions.isEmpty()) {
            null
        } else {
            getPreviewResolution(sensorResolution)
        }

        fun getPreviewResolution(resolution: Resolution): Resolution {
            return previewLessOrEqualsResolutions.firstOrNull { it.ratio0 == resolution.ratio0 }
                ?: previewLessOrEqualsResolutions.first()
        }

        //*************************************************************************************************************

        private val jpegSizes: Array<Size>? = scalerStreamConfigurationMap.getOutputSizes(ImageFormat.JPEG)

        val photoResolutions: List<Resolution> = run {
            (jpegSizes ?: emptyArray())
                .filter { it.width > 0 && it.height > 0 }
                .map { Resolution(it.width, it.height, sensorOrientation) }
                .toSet()
                .sortedDescending()
        }

        private val defaultPhotoResolution: Resolution? = if (photoResolutions.isEmpty()) {
            null
        } else {
            photoResolutions.firstOrNull { it.ratio0 == sensorResolution.ratio0 }
                ?: photoResolutions.first()
        }

        fun requireDefaultPhotoResolution(): Resolution {
            return defaultPhotoResolution ?: throw RuntimeException()
        }

        fun getPhotoResolution(entryValue: String): Resolution {
            return photoResolutions.firstOrNull { it.entryValue == entryValue } ?: requireDefaultPhotoResolution()
        }

        //*************************************************************************************************************

        private val mediaRecorderSizes: Array<Size>? =
            scalerStreamConfigurationMap.getOutputSizes(MediaRecorder::class.java)

        val videoProfiles: List<VideoProfile> = run {
            CAMCORDER_PROFILE_QUALITIES
                .asSequence()
                .filter { CamcorderProfile.hasProfile(oldId, it) }
                .map { CamcorderProfile.get(oldId, it) }
                .filter { mediaRecorderSizes?.contains(Size(it.videoFrameWidth, it.videoFrameHeight)) == true }
                .filter { it.videoFrameWidth > 0 && it.videoFrameHeight > 0 }
                .map { VideoProfile(it, sensorOrientation) }
                .toSet()
                .sortedDescending()
        }

        private val defaultVideoProfile: VideoProfile? = if (videoProfiles.isEmpty()) {
            null
        } else {
            videoProfiles.firstOrNull {
                it.camcorderProfile.quality == CamcorderProfile.QUALITY_1080P ||
                        it.camcorderProfile.quality == CamcorderProfile.QUALITY_HIGH
            } ?: videoProfiles.first()
        }

        fun requireDefaultVideoProfile(): VideoProfile {
            return defaultVideoProfile ?: throw RuntimeException()
        }

        fun getVideoProfile(entryValue: String): VideoProfile {
            return videoProfiles.firstOrNull { it.entryValue == entryValue } ?: requireDefaultVideoProfile()
        }

        //*************************************************************************************************************

        fun isValid(): Boolean {
            return !isExternal &&
                    previewLessOrEqualsResolutions.isNotEmpty() &&
                    photoResolutions.isNotEmpty() &&
                    videoProfiles.isNotEmpty()
        }

        fun getInvalidString(): String {
            if (isValid()) return "{$id}"
            return "{$id,$isExternal,${previewLessOrEqualsResolutions.size},${photoResolutions.size},${videoProfiles.size}}"
        }

        //*************************************************************************************************************

        fun report(): Report.Camera.Device {
            val device = Report.Camera.Device()
            device.id = id
            device.oldId = oldId
            device.lensFacing = when (lensFacing) {
                CameraMetadata.LENS_FACING_FRONT -> "FRONT"
                CameraMetadata.LENS_FACING_BACK -> "BACK"
                CameraMetadata.LENS_FACING_EXTERNAL -> "EXTERNAL"
                else -> "else"
            }
            device.sensorOrientation = sensorOrientation
            device.sensorResolution = sensorResolution.report()
            device.surfaceTextureSizes = surfaceTextureSizes?.map { "${it.width}x${it.height}" }
            device.previewNotGreaterResolutions = previewNotGreaterResolutions.map { it.report() }
            device.previewLessOrEqualsResolutions = previewLessOrEqualsResolutions.map { it.report() }
            device.defaultPreviewResolution = defaultPreviewResolution?.report()
            device.jpegSizes = jpegSizes?.map { "${it.width}x${it.height}" }
            device.photoResolutions = photoResolutions.map { it.report() }
            device.defaultPhotoResolution = defaultPhotoResolution?.report()
            device.mediaRecorderSizes = mediaRecorderSizes?.map { "${it.width}x${it.height}" }
            device.videoProfiles = videoProfiles.map { it.report() }
            device.defaultVideoProfile = defaultVideoProfile?.report()
            return device
        }

        //*************************************************************************************************************

        /**
         * 照片分辨率, 预览分辨率.
         */
        open class Resolution(width: Int, height: Int, sensorOrientation: Int) :
            RotationSize(width, height, sensorOrientation / 90) {
            /**
             * 用于 SharedPreferences.
             */
            val entryValue: String = "${width}_$height"

            /**
             * 百万像素.
             */
            val megapixel: Float = area / 1_000_000f

            //*********************************************************************************************************

            open fun report(): Report.Camera.Device.Resolution {
                val resolution = Report.Camera.Device.Resolution()
                resolution.entryValue = entryValue
                resolution.ratio = "${ratio.width}:${ratio.height}"
                return resolution
            }
        }

        //*************************************************************************************************************

        /**
         * 视频配置.
         */
        class VideoProfile(val camcorderProfile: CamcorderProfile, sensorOrientation: Int) :
            Resolution(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight, sensorOrientation) {
            val qualityString: String = when (camcorderProfile.quality) {
                CamcorderProfile.QUALITY_LOW -> "low"
                CamcorderProfile.QUALITY_HIGH -> "high"
                CamcorderProfile.QUALITY_QCIF -> "QCIF"
                CamcorderProfile.QUALITY_CIF -> "CIF"
                CamcorderProfile.QUALITY_480P -> "480P"
                CamcorderProfile.QUALITY_720P -> "720P"
                CamcorderProfile.QUALITY_1080P -> "1080P"
                CamcorderProfile.QUALITY_QVGA -> "QVGA"
                CamcorderProfile.QUALITY_2160P -> "2160P"
                else -> "else"
            }

            /**
             * 文件后缀名. 默认 .mp4.
             */
            val extension: String = when (camcorderProfile.fileFormat) {
                MediaRecorder.OutputFormat.THREE_GPP -> ".3gp"
                else -> ".mp4"
            }

            //*********************************************************************************************************

            override fun report(): Report.Camera.Device.VideoProfile {
                val videoProfile = Report.Camera.Device.VideoProfile()
                val resolution = super.report()
                videoProfile.entryValue = resolution.entryValue
                videoProfile.ratio = resolution.ratio
                videoProfile.duration = camcorderProfile.duration
                videoProfile.quality = qualityString
                videoProfile.fileFormat = when (camcorderProfile.fileFormat) {
                    MediaRecorder.OutputFormat.DEFAULT -> "default"
                    MediaRecorder.OutputFormat.THREE_GPP -> "THREE_GPP"
                    MediaRecorder.OutputFormat.MPEG_4 -> "MPEG_4"
                    MediaRecorder.OutputFormat.AMR_NB -> "AMR_NB"
                    MediaRecorder.OutputFormat.AMR_WB -> "AMR_WB"
                    MediaRecorder.OutputFormat.AAC_ADTS -> "AAC_ADTS"
                    MediaRecorder.OutputFormat.MPEG_2_TS -> "MPEG_2_TS"
                    MediaRecorder.OutputFormat.WEBM -> "WEBM"
                    else -> "else"
                }
                videoProfile.videoCodec = when (camcorderProfile.videoCodec) {
                    MediaRecorder.VideoEncoder.DEFAULT -> "default"
                    MediaRecorder.VideoEncoder.H263 -> "H263"
                    MediaRecorder.VideoEncoder.H264 -> "H264"
                    MediaRecorder.VideoEncoder.MPEG_4_SP -> "MPEG_4_SP"
                    MediaRecorder.VideoEncoder.VP8 -> "VP8"
                    MediaRecorder.VideoEncoder.HEVC -> "HEVC"
                    else -> "else"
                }
                videoProfile.videoBitRate = camcorderProfile.videoBitRate
                videoProfile.videoFrameRate = camcorderProfile.videoFrameRate
                videoProfile.audioCodec = when (camcorderProfile.audioCodec) {
                    MediaRecorder.AudioEncoder.DEFAULT -> "default"
                    MediaRecorder.AudioEncoder.AMR_NB -> "AMR_NB"
                    MediaRecorder.AudioEncoder.AMR_WB -> "AMR_WB"
                    MediaRecorder.AudioEncoder.AAC -> "AAC"
                    MediaRecorder.AudioEncoder.HE_AAC -> "HE_AAC"
                    MediaRecorder.AudioEncoder.AAC_ELD -> "AAC_ELD"
                    MediaRecorder.AudioEncoder.VORBIS -> "VORBIS"
                    else -> "else"
                }
                videoProfile.audioBitRate = camcorderProfile.audioBitRate
                videoProfile.audioSampleRate = camcorderProfile.audioSampleRate
                videoProfile.audioChannels = camcorderProfile.audioChannels
                return videoProfile
            }
        }
    }
}
