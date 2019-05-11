@file:Suppress("DEPRECATION")

package com.ebnbin.windowcamera.camera

import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.util.Size
import android.view.Surface
import com.ebnbin.eb.util.EBModel
import com.ebnbin.eb.util.RotationSize
import com.ebnbin.eb.util.SystemServices
import com.ebnbin.eb.util.WindowHelper

/**
 * 相机帮助类.
 *
 * 需要 try catch Throwable 避免 ExceptionInInitializerError.
 *
 * 在使用前必须调用 [isValid] 检测有效性.
 *
 * 需要 camera 权限.
 */
class CameraHelper private constructor() : EBModel {
    private val ids: List<String> = SystemServices.cameraManager.cameraIdList.toList()

    /**
     * 对所有摄像头进行检测, 但只使用第一个后置摄像头和第一个前置摄像头, 且后置摄像头和前置摄像头都必须存在.
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

    @Transient
    private var backDevice: Device? = null
    @Transient
    private var frontDevice: Device? = null

    // For report.
    private var backDeviceId: String? = null
    private var frontDeviceId: String? = null

    init {
        devices.forEach {
            if (!it.isValid()) return@forEach
            if (it.isFront) {
                if (frontDevice == null) {
                    frontDevice = it
                    frontDeviceId = it.id
                }
            } else {
                if (backDevice == null) {
                    backDevice = it
                    backDeviceId = it.id
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

    /**
     * 在使用 CameraHelper 前必须调用.
     */
    fun isValid(): Boolean {
        return backDevice != null && frontDevice != null
    }

    /**
     * 摄像头.
     *
     * @param id Camera2 API id.
     *
     * @param oldId Camera API id.
     */
    class Device(val id: String, private val oldId: Int) : EBModel {
        @Transient
        private val cameraCharacteristics: CameraCharacteristics =
            SystemServices.cameraManager.getCameraCharacteristics(id)

        //*************************************************************************************************************

        @Transient
        private val oldCamera: Camera = Camera.open(oldId)

        @Transient
        private val oldParameters: Camera.Parameters = oldCamera.parameters

        private val oldPreferredPreviewSizeForVideo: Camera.Size? = oldParameters.preferredPreviewSizeForVideo

        init {
            oldCamera.release()
        }

        //*************************************************************************************************************

        @Transient
        private val lensFacing: Int = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) as Int

        // For report.
        private val lensFacingString: String = when (lensFacing) {
            CameraMetadata.LENS_FACING_FRONT -> "FRONT"
            CameraMetadata.LENS_FACING_BACK -> "BACK"
            CameraMetadata.LENS_FACING_EXTERNAL -> "EXTERNAL"
            else -> "else"
        }

        /**
         * 外置摄像头.
         */
        @Transient
        private val isExternal: Boolean = lensFacing != CameraMetadata.LENS_FACING_FRONT &&
                lensFacing != CameraMetadata.LENS_FACING_BACK

        /**
         * 后置摄像头和外置摄像头都为 false.
         */
        @Transient
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

        @Transient
        private val scalerStreamConfigurationMap: StreamConfigurationMap =
            cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) as StreamConfigurationMap

        @Transient
        private val jpegSizes: Array<Size>? = scalerStreamConfigurationMap.getOutputSizes(ImageFormat.JPEG)

        // For report.
        private val jpegSizeStrings: Array<String>? = jpegSizes
            ?.map { "${it.width}x${it.height}" }
            ?.toTypedArray()

        val photoResolutions: List<Resolution> = run {
            (jpegSizes ?: emptyArray())
                .filter { it.width > 0 && it.height > 0 }
                .map { Resolution(it.width, it.height, sensorOrientation) }
                .toSet()
                .toList()
                .sortedDescending()
        }

        private var defaultPhotoResolution: Resolution? = null
        init {
            if (photoResolutions.isNotEmpty()) {
                defaultPhotoResolution = photoResolutions.first()
            }
        }

        fun requireDefaultPhotoResolution(): Resolution {
            return defaultPhotoResolution ?: throw RuntimeException()
        }

        fun getPhotoResolution(entryValue: String): Resolution {
            return photoResolutions.first { it.entryValue == entryValue }
        }

        /**
         * 照片分辨率, 预览分辨率.
         */
        open class Resolution(width: Int, height: Int, sensorOrientation: Int) :
            RotationSize(width, height, sensorOrientation / 90) {
            /**
             * 百万像素.
             */
            @Transient
            val megapixel: Float = area / 1_000_000f

            /**
             * 用于 SharedPreferences.
             */
            @Transient
            val entryValue: String = "${width}_$height"
        }

        //*************************************************************************************************************

        @Transient
        private val surfaceTextureSizes: Array<Size>? =
            scalerStreamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)

        // For report.
        private val surfaceTextureSizeStrings: Array<String>? = surfaceTextureSizes
            ?.map { "${it.width}x${it.height}" }
            ?.toTypedArray()

        val previewResolutions: List<Resolution> = run {
            val displayRealSize = WindowHelper.displayRealSize
            val preferredPreviewResolution = oldPreferredPreviewSizeForVideo?.let {
                Resolution(it.width, it.height, sensorOrientation)
            }
            val linkedHashSet = LinkedHashSet<Resolution>()
            (surfaceTextureSizes ?: emptyArray())
                .filter { it.width > 0 && it.height > 0 }
                .map { Resolution(it.width, it.height, sensorOrientation) }
                .filter {
                    if (preferredPreviewResolution == null) {
                        true
                    } else {
                        it.isWidthHeightLessOrEquals(preferredPreviewResolution)
                    }
                }
                .sorted()
                .run outer@{
                    forEach {
                        linkedHashSet.add(it)
                        if (it.isWidthHeightGreaterOrEquals(displayRealSize)) return@outer
                    }
                }
            linkedHashSet
                .sortedDescending()
                .toList()
        }

        fun getPreviewResolution(captureResolution: Resolution): Resolution {
            return previewResolutions
                .firstOrNull { it.ratio == captureResolution.ratio }
                ?: previewResolutions.first()
        }

        //*************************************************************************************************************

        @Transient
        private val mediaRecorderSizes: Array<Size>? =
            scalerStreamConfigurationMap.getOutputSizes(MediaRecorder::class.java)

        // For report.
        private val mediaRecorderSizeStrings: Array<String>? = mediaRecorderSizes
            ?.map { "${it.width}x${it.height}" }
            ?.toTypedArray()

        val videoProfiles: List<VideoProfile> = run {
            CAMCORDER_PROFILE_QUALITIES
                .asSequence()
                .filter { CamcorderProfile.hasProfile(oldId, it) }
                .map { CamcorderProfile.get(oldId, it) }
                .filter { mediaRecorderSizes?.contains(Size(it.videoFrameWidth, it.videoFrameHeight)) == true }
                .map { VideoProfile(it, sensorOrientation) }
                .toSet()
                .toList()
                .sortedDescending()
        }

        private var defaultVideoProfile: VideoProfile? = null
        init {
            if (videoProfiles.isNotEmpty()) {
                defaultVideoProfile = videoProfiles.firstOrNull {
                    it.camcorderProfile.quality == CamcorderProfile.QUALITY_1080P ||
                            it.camcorderProfile.quality == CamcorderProfile.QUALITY_HIGH
                } ?: videoProfiles.first()
            }
        }

        fun requireDefaultVideoProfile(): VideoProfile {
            return defaultVideoProfile ?: throw RuntimeException()
        }

        fun getVideoProfile(entryValue: String): VideoProfile {
            return videoProfiles.first { it.entryValue == entryValue }
        }

        /**
         * 视频配置.
         */
        open class VideoProfile(@Transient val camcorderProfile: CamcorderProfile, sensorOrientation: Int) :
            Resolution(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight, sensorOrientation) {
            // For report.
            private val duration: Int = camcorderProfile.duration

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

            // For report.
            private val fileFormatString: String = when (camcorderProfile.fileFormat) {
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

            // For report.
            private val videoCodecString: String = when (camcorderProfile.videoCodec) {
                MediaRecorder.VideoEncoder.DEFAULT -> "default"
                MediaRecorder.VideoEncoder.H263 -> "H263"
                MediaRecorder.VideoEncoder.H264 -> "H264"
                MediaRecorder.VideoEncoder.MPEG_4_SP -> "MPEG_4_SP"
                MediaRecorder.VideoEncoder.VP8 -> "VP8"
                MediaRecorder.VideoEncoder.HEVC -> "HEVC"
                else -> "else"
            }

            // For report.
            private val videoBitRate: Int = camcorderProfile.videoBitRate

            // For report.
            private val videoFrameRate: Int = camcorderProfile.videoFrameRate

            // For report.
            private val audioCodecString: String = when (camcorderProfile.audioCodec) {
                MediaRecorder.AudioEncoder.DEFAULT -> "default"
                MediaRecorder.AudioEncoder.AMR_NB -> "AMR_NB"
                MediaRecorder.AudioEncoder.AMR_WB -> "AMR_WB"
                MediaRecorder.AudioEncoder.AAC -> "AAC"
                MediaRecorder.AudioEncoder.HE_AAC -> "HE_AAC"
                MediaRecorder.AudioEncoder.AAC_ELD -> "AAC_ELD"
                MediaRecorder.AudioEncoder.VORBIS -> "VORBIS"
                else -> "else"
            }

            // For report.
            private val audioBitRate: Int = camcorderProfile.audioBitRate

            // For report.
            private val audioSampleRate: Int = camcorderProfile.audioSampleRate

            // For report.
            private val audioChannels: Int = camcorderProfile.audioChannels

            /**
             * 文件后缀名. 默认 .mp4.
             */
            @Transient
            val extension: String = when (camcorderProfile.fileFormat) {
                MediaRecorder.OutputFormat.THREE_GPP -> ".3gp"
                else -> ".mp4"
            }
        }

        //*************************************************************************************************************

        fun isValid(): Boolean {
            return !isExternal &&
                    photoResolutions.isNotEmpty() &&
                    previewResolutions.isNotEmpty() &&
                    videoProfiles.isNotEmpty()
        }
    }

    companion object {
        val instance: CameraHelper by lazy { CameraHelper() }

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
    }
}
