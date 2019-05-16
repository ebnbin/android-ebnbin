package com.ebnbin.windowcamera.test

class Report0_2_0 : Report() {
    lateinit var timestamp: String
    lateinit var manufacturer: String
    lateinit var model: String
    var sdk: Int = 0
    lateinit var displayRealSize: RotationSize
    lateinit var displaySize: RotationSize
    var density: Float = 0f
    lateinit var displayRealSizeDp: String
    lateinit var locale: String
    lateinit var abi: String
    lateinit var cameraHelper: CameraHelper

    open class RotationSize {
        var width: Int = 0
        var height: Int = 0
        var rotation: Int = 0
        var ratioWidth: Int = 0
        var ratioHeight: Int = 0
    }

    class CameraHelper {
        lateinit var ids: List<String>
        lateinit var devices: List<Device>
        var backDeviceId: String? = null
        var frontDeviceId: String? = null

        class Device {
            lateinit var id: String
            var oldId: Int = 0
            var oldPreferredPreviewSizeForVideo: Size? = null
            var oldSupportedPictureSizes: List<Size>? = null
            var oldSupportedPreviewSizes: List<Size>? = null
            var oldSupportedVideoSizes: List<Size>? = null
            lateinit var lensFacingString: String
            var sensorOrientation: Int = 0
            lateinit var sensorOrientations: List<Int>
            var jpegSizeStrings: List<String>? = null
            lateinit var photoResolutions: List<Resolution>
            var defaultPhotoResolution: Resolution? = null
            var surfaceTextureSizeStrings: List<String>? = null
            lateinit var previewResolutions: List<Resolution>
            var mediaRecorderSizeStrings: List<String>? = null
            lateinit var videoProfiles: List<VideoProfile>
            var defaultVideoProfile: VideoProfile? = null
            var rawSensorSizeStrings: List<String>? = null

            class Size {
                var width: Int = 0
                var height: Int = 0
            }

            open class Resolution : RotationSize()

            class VideoProfile : Resolution() {
                var duration: Int = 0
                lateinit var qualityString: String
                lateinit var fileFormatString: String
                lateinit var videoCodecString: String
                var videoBitRate: Int = 0
                var videoFrameRate: Int = 0
                lateinit var audioCodecString: String
                var audioBitRate: Int = 0
                var audioSampleRate: Int = 0
                var audioChannels: Int = 0
            }
        }
    }
}
