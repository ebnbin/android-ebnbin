package com.ebnbin.windowcamera.test

import com.google.gson.Gson
import java.io.File

private val reports: ArrayList<Report> = ArrayList()
private val report0_1_0s: ArrayList<Report0_1_0> = ArrayList()
private val report0_2_0s: ArrayList<Report0_2_0> = ArrayList()
private val report0_3_0s: ArrayList<Report0_3_0> = ArrayList()

private fun init() {
    val gson = Gson()
    File("../api-android-ebnbin/windowcamera/report/").listFiles()
        .forEach {
            val text = it.readText()
            val report = gson.fromJson(text, Report::class.java)
            when (report.version) {
                "0.1.0" -> {
                    val report0_1_0 = gson.fromJson(text, Report0_1_0::class.java)
                    reports.add(report0_1_0)
                    report0_1_0s.add(report0_1_0)
                }
                "0.2.0" -> {
                    val report0_2_0 = gson.fromJson(text, Report0_2_0::class.java)
                    reports.add(report0_2_0)
                    report0_2_0s.add(report0_2_0)
                }
                "0.3.0" -> {
                    val report0_3_0 = gson.fromJson(text, Report0_3_0::class.java)
                    reports.add(report0_3_0)
                    report0_3_0s.add(report0_3_0)
                }
                else -> {
                    reports.add(report)
                }
            }
        }
}

private fun mapKeys(
    getKeys0_1_0: (Report0_1_0) -> List<String?>,
    getKeys0_2_0: (Report0_2_0) -> List<String?>,
    getKeys0_3_0: (Report0_3_0) -> List<String?>
): Map<String, List<Report>> {
    val map = HashMap<String, ArrayList<Report>>()
    reports.forEach {
        val keys = when (it.version) {
            "0.1.0" -> getKeys0_1_0(it as Report0_1_0)
            "0.2.0" -> getKeys0_2_0(it as Report0_2_0)
            "0.3.0" -> getKeys0_3_0(it as Report0_3_0)
            else -> return@forEach
        }
        keys.forEach { key ->
            map[key ?: "null"] = map.getOrDefault(key ?: "null", ArrayList()).apply {
                add(it)
            }
        }
    }
    return map.toSortedMap(Comparator { o1, o2 ->
        val compareSize = map.getValue(o2).size - map.getValue(o1).size
        if (compareSize == 0) {
            o1.hashCode() - o2.hashCode()
        } else {
            compareSize
        }
    })
}

private fun deviceId(): Map<String, List<Report>> {
    return mapKeys(
        { listOf(it.deviceId?.substring(0, 2)) },
        { listOf(it.deviceId?.substring(0, 2)) },
        { listOf(it.deviceId?.substring(0, 2)) }
    )
}

private fun version(): Map<String, List<Report>> {
    return mapKeys(
        { listOf(it.version) },
        { listOf(it.version) },
        { listOf(it.version) }
    )
}


private fun sdk(): Map<String, List<Report>> {
    return mapKeys(
        { listOf(it.sdk.toString()) },
        { listOf(it.sdk.toString()) },
        { listOf(it.sdk.toString()) }
    )
}

private fun manufacturer(): Map<String, List<Report>> {
    return mapKeys(
        { listOf(it.manufacturer) },
        { listOf(it.manufacturer) },
        { listOf(it.manufacturer) }
    )
}

private fun model(): Map<String, List<Report>> {
    return mapKeys(
        { listOf(it.model) },
        { listOf(it.model) },
        { listOf(it.model) }
    )
}
//
//private fun displayRealSize(): Map<String, List<Report>> {
//    return mapKey(
//        { it.displayRealSize },
//        {
//            it.displayRealSize.run {
//                val width0 = if (rotation == 0 || rotation == 2) width else height
//                val height0 = if (rotation == 0 || rotation == 2) height else width
//                "${width0}x$height0"
//            }
//        }
//    )
//}
//
//private fun displaySize(): Map<String, List<Report>> {
//    return mapKey(
//        { it.displaySize },
//        {
//            it.displaySize.run {
//                val width0 = if (rotation == 0 || rotation == 2) width else height
//                val height0 = if (rotation == 0 || rotation == 2) height else width
//                "${width0}x$height0"
//            }
//        }
//    )
//}
//
//private fun density(): Map<String, List<Report>> {
//    return mapKey(
//        { it.density.toString() },
//        { it.density.toString() }
//    )
//}
//
//private fun displayRealSizeDp(): Map<String, List<Report>> {
//    return mapKey(
//        { it.displayRealSizeDp },
//        { it.displayRealSizeDp }
//    )
//}
//
//private fun locale(): Map<String, List<Report>> {
//    return mapKey(
//        { it.locale },
//        { it.locale }
//    )
//}
//
//private fun abi(): Map<String, List<Report>> {
//    return mapKey(
//        { it.abi },
//        { it.abi }
//    )
//}
//
//private fun cameraIds(): Map<String, List<Report>> {
//    return mapKey(
//        { it.cameraHelper.substringAfter("ids:", "").substringBefore("]", "") + "]" },
//        { it.cameraHelper.ids.joinToString(",", "[", "]") }
//    )
//}
//
//private fun cameraBackDeviceId(): Map<String, List<Report>> {
//    return mapKey(
//        { it.cameraHelper.substringAfter("backDevice:", "").substringBefore(",").substringBefore("}") },
//        { it.cameraHelper.backDeviceId ?: "null" }
//    )
//}
//
//private fun cameraFrontDeviceId(): Map<String, List<Report>> {
//    return mapKey(
//        { it.cameraHelper.substringAfter("frontDevice:", "").substringBefore(",").substringBefore("}") },
//        { it.cameraHelper.frontDeviceId ?: "null" }
//    )
//}
//
//private fun cameraBackOldPreferredPreviewSizeForVideo(): Map<String, List<Report>> {
//    return mapKey(
//        { "0.1.0" },
//        { it.cameraHelper.devices.getOrNull(0)?.oldPreferredPreviewSizeForVideo?.run { "${width}x$height" } ?: "null" }
//    )
//}
//
//private fun cameraFrontOldPreferredPreviewSizeForVideo(): Map<String, List<Report>> {
//    return mapKey(
//        { "0.1.0" },
//        { it.cameraHelper.devices.getOrNull(1)?.oldPreferredPreviewSizeForVideo?.run { "${width}x$height" } ?: "null" }
//    )
//}
//
//private fun cameraBackFirstOldSupportedPictureSize(): Map<String, List<Report>> {
//    return mapKey(
//        { "0.1.0" },
//        { it.cameraHelper.devices.getOrNull(0)?.oldSupportedPictureSizes?.firstOrNull()?.run { "${width}x$height" } ?: "null" }
//    )
//}
//
//private fun cameraFrontFirstOldSupportedPictureSize(): Map<String, List<Report>> {
//    return mapKey(
//        { "0.1.0" },
//        { it.cameraHelper.devices.getOrNull(1)?.oldSupportedPictureSizes?.firstOrNull()?.run { "${width}x$height" } ?: "null" }
//    )
//}
//
//private fun cameraBackFirstOldSupportedPreviewSize(): Map<String, List<Report>> {
//    return mapKey(
//        { "0.1.0" },
//        { it.cameraHelper.devices.getOrNull(0)?.oldSupportedPreviewSizes?.firstOrNull()?.run { "${width}x$height" } ?: "null" }
//    )
//}
//
//private fun cameraFrontFirstOldSupportedPreviewSize(): Map<String, List<Report>> {
//    return mapKey(
//        { "0.1.0" },
//        { it.cameraHelper.devices.getOrNull(1)?.oldSupportedPreviewSizes?.firstOrNull()?.run { "${width}x$height" } ?: "null" }
//    )
//}
//
//private fun cameraBackFirstOldSupportedVideoSize(): Map<String, List<Report>> {
//    return mapKey(
//        { "0.1.0" },
//        { it.cameraHelper.devices.getOrNull(0)?.oldSupportedVideoSizes?.firstOrNull()?.run { "${width}x$height" } ?: "null" }
//    )
//}
//
//private fun cameraFrontFirstOldSupportedVideoSize(): Map<String, List<Report>> {
//    return mapKey(
//        { "0.1.0" },
//        { it.cameraHelper.devices.getOrNull(1)?.oldSupportedVideoSizes?.firstOrNull()?.run { "${width}x$height" } ?: "null" }
//    )
//}
//
//private fun cameraLensFacings(): Map<String, List<Report>> {
//    return mapKey(
//        {
//            val lensFacing0 = it.cameraHelper.substringAfter("oldId:0,lensFacing:", "").substringBefore(",")
//            val lensFacing1 = it.cameraHelper.substringAfter("oldId:1,lensFacing:", "").substringBefore(",")
//            val lensFacing2 = it.cameraHelper.substringAfter("oldId:2,lensFacing:", "").substringBefore(",")
//            val lensFacing3 = it.cameraHelper.substringAfter("oldId:3,lensFacing:", "").substringBefore(",")
//            val lensFacing4 = it.cameraHelper.substringAfter("oldId:4,lensFacing:", "").substringBefore(",")
//            "$lensFacing0,$lensFacing1,$lensFacing2,$lensFacing3,$lensFacing4"
//        },
//        {
//            val lensFacing0 = it.cameraHelper.devices.getOrNull(0)?.lensFacingString ?: ""
//            val lensFacing1 = it.cameraHelper.devices.getOrNull(1)?.lensFacingString ?: ""
//            val lensFacing2 = it.cameraHelper.devices.getOrNull(2)?.lensFacingString ?: ""
//            val lensFacing3 = it.cameraHelper.devices.getOrNull(3)?.lensFacingString ?: ""
//            val lensFacing4 = it.cameraHelper.devices.getOrNull(4)?.lensFacingString ?: ""
//            "$lensFacing0,$lensFacing1,$lensFacing2,$lensFacing3,$lensFacing4"
//        }
//    )
//}
//
//private fun cameraBackFirstPhotoRatio(): Map<String, List<Report>> {
//    return mapKey(
//        { it.cameraHelper.substringAfter("photoResolutions:[{", "").substringAfter(",ratio:", "").substringBefore("}") },
//        {
//            it.cameraHelper.devices.getOrNull(0)?.photoResolutions?.firstOrNull()?.run {
//                "${ratioWidth}_$ratioHeight"
//            } ?: ""
//        }
//    )
//}
//
//private fun cameraBackFirstPreviewRatio(): Map<String, List<Report>> {
//    return mapKey(
//        { it.cameraHelper.substringAfter("previewResolutions:[{", "").substringAfter(",ratio:", "").substringBefore("}") },
//        {
//            it.cameraHelper.devices.getOrNull(0)?.previewResolutions?.firstOrNull()?.run {
//                "${ratioWidth}_$ratioHeight"
//            } ?: ""
//        }
//    )
//}
//
//private fun cameraBackFirstVideoRatio(): Map<String, List<Report>> {
//    return mapKey(
//        { it.cameraHelper.substringAfter("videoProfiles:[{", "").substringAfter(",ratio:", "").substringBefore(",") },
//        {
//            it.cameraHelper.devices.getOrNull(0)?.videoProfiles?.firstOrNull()?.run {
//                "${ratioWidth}_$ratioHeight"
//            } ?: ""
//        }
//    )
//}

fun main() {
    init()

    val deviceId = deviceId()
    val version = version()
    val sdk = sdk()
    val manufacturer = manufacturer()
    val model = model()
//    val displayRealSize = displayRealSize()
//    val displaySize = displaySize()
//    val density = density()
//    val displayRealSizeDp = displayRealSizeDp()
//    val locale = locale()
//    val abi = abi()
//    val cameraIds = cameraIds()
//    val cameraBackDeviceId = cameraBackDeviceId()
//    val cameraFrontDeviceId = cameraFrontDeviceId()
//    val cameraBackOldPreferredPreviewSizeForVideo = cameraBackOldPreferredPreviewSizeForVideo()
//    val cameraFrontOldPreferredPreviewSizeForVideo = cameraFrontOldPreferredPreviewSizeForVideo()
//    val cameraBackFirstOldSupportedPictureSize = cameraBackFirstOldSupportedPictureSize()
//    val cameraFrontFirstOldSupportedPictureSize = cameraFrontFirstOldSupportedPictureSize()
//    val cameraBackFirstOldSupportedPreviewSize = cameraBackFirstOldSupportedPreviewSize()
//    val cameraFrontFirstOldSupportedPreviewSize = cameraFrontFirstOldSupportedPreviewSize()
//    val cameraBackFirstOldSupportedVideoSize = cameraBackFirstOldSupportedVideoSize()
//    val cameraFrontFirstOldSupportedVideoSize = cameraFrontFirstOldSupportedVideoSize()
//    val cameraLensFacings = cameraLensFacings()
//    val cameraBackFirstPhotoRatio = cameraBackFirstPhotoRatio()
//    val cameraBackFirstPreviewRatio = cameraBackFirstPreviewRatio()
//    val cameraBackFirstVideoRatio = cameraBackFirstVideoRatio()
}
