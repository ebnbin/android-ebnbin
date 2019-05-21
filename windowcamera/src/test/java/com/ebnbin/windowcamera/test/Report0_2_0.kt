package com.ebnbin.windowcamera.test

class Report0_2_0 : Report() {
    var timestamp: String? = null
    var manufacturer: String? = null
    var model: String? = null
    var sdk: Int = 0
    var displayRealSize: RotationSize? = null
    var displaySize: RotationSize? = null
    var density: Float = 0f
    var displayRealSizeDp: String? = null
    var locale: String? = null
    var abi: String? = null
    var cameraHelper: CameraHelper? = null

    open class RotationSize {
        var width: Int = 0
        var height: Int = 0
        var rotation: Int = 0
        var ratioWidth: Int = 0
        var ratioHeight: Int = 0
    }

    class CameraHelper {
        var ids: List<String>? = null
        var devices: List<Device>? = null
        var backDeviceId: String? = null
        var frontDeviceId: String? = null

        class Device {
            var id: String? = null
            var oldId: Int = 0
            var oldPreferredPreviewSizeForVideo: Size? = null
            var oldSupportedPictureSizes: List<Size>? = null
            var oldSupportedPreviewSizes: List<Size>? = null
            var oldSupportedVideoSizes: List<Size>? = null
            var lensFacingString: String? = null
            var sensorOrientation: Int = 0
            var sensorOrientations: List<Int>? = null
            var jpegSizeStrings: List<String>? = null
            var photoResolutions: List<Resolution>? = null
            var defaultPhotoResolution: Resolution? = null
            var surfaceTextureSizeStrings: List<String>? = null
            var previewResolutions: List<Resolution>? = null
            var mediaRecorderSizeStrings: List<String>? = null
            var videoProfiles: List<VideoProfile>? = null
            var defaultVideoProfile: VideoProfile? = null
            var rawSensorSizeStrings: List<String>? = null

            class Size {
                var width: Int = 0
                var height: Int = 0
            }

            open class Resolution : RotationSize()

            class VideoProfile : Resolution() {
                var duration: Int = 0
                var qualityString: String? = null
                var fileFormatString: String? = null
                var videoCodecString: String? = null
                var videoBitRate: Int = 0
                var videoFrameRate: Int = 0
                var audioCodecString: String? = null
                var audioBitRate: Int = 0
                var audioSampleRate: Int = 0
                var audioChannels: Int = 0
            }
        }
    }
}
