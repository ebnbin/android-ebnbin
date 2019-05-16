package com.ebnbin.windowcamera.dev

import com.ebnbin.eb.dev.EBReport
import com.ebnbin.eb.util.EBModel
import com.ebnbin.windowcamera.camera.CameraHelper

class Report : EBReport() {
    var camera: Camera? = null

    class Camera : EBModel {
        var ids: List<String>? = null
        var devices: List<Device>? = null
        var backDevice: String? = null
        var frontDevice: String? = null

        class Device : EBModel {
            var id: String? = null
            var oldId: Int? = null
            var oldSupportedPreviewSizes: List<String>? = null
            var oldSupportedPictureSizes: List<String>? = null
            var oldSupportedVideoSizes: List<String>? = null
            var lensFacing: String? = null
            var sensorOrientation: Int? = null
            var sensorResolution: Resolution? = null
            var surfaceTextureSizes: List<String>? = null
            var previewResolutions: List<Resolution>? = null
            var defaultPreviewResolution: Resolution? = null
            var jpegSizes: List<String>? = null
            var photoResolutions: List<Resolution>? = null
            var defaultPhotoResolution: Resolution? = null
            var mediaRecorderSizes: List<String>? = null
            var videoProfiles: List<VideoProfile>? = null
            var defaultVideoProfile: VideoProfile? = null
            var rawSensorSizes: List<String>? = null

            open class Resolution : EBModel {
                var entryValue: String? = null
                var ratio: String? = null
            }

            class VideoProfile : Resolution() {
                var duration: Int? = null
                var quality: String? = null
                var fileFormat: String? = null
                var videoCodec: String? = null
                var videoBitRate: Int? = null
                var videoFrameRate: Int? = null
                var audioCodec: String? = null
                var audioBitRate: Int? = null
                var audioSampleRate: Int? = null
                var audioChannels: Int? = null
            }
        }
    }

    fun create(cameraHelper: CameraHelper?): Report {
        create()
        camera = cameraHelper?.report()
        return this
    }
}
