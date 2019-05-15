package com.ebnbin.windowcamera.test

import com.google.gson.Gson
import java.io.File

private val reports: ArrayList<Report> = ArrayList()
private val report0_1_0s: ArrayList<Report0_1_0> = ArrayList()
private val report0_2_0s: ArrayList<Report0_2_0> = ArrayList()

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
                else -> {
                    reports.add(report)
                }
            }
        }
}

private fun map(
    getKey0_1_0: (Report0_1_0) -> String,
    getKey0_2_0: (Report0_2_0) -> String
): Map<String, List<Report>> {
    val map = HashMap<String, ArrayList<Report>>()
    reports.forEach {
        val key = when (it.version) {
            "0.1.0" -> getKey0_1_0(it as Report0_1_0)
            "0.2.0" -> getKey0_2_0(it as Report0_2_0)
            else -> return@forEach
        }
        map[key] = map.getOrDefault(key, ArrayList()).apply {
            add(it)
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

private fun version(): Map<String, List<Report>> {
    return map(
        { it.version },
        { it.version }
    )
}

private fun sdk(): Map<String, List<Report>> {
    return map(
        { it.sdk.toString() },
        { it.sdk.toString() }
    )
}

private fun manufacturer(): Map<String, List<Report>> {
    return map(
        { it.manufacturer },
        { it.manufacturer }
    )
}

private fun model(): Map<String, List<Report>> {
    return map(
        { it.model },
        { it.model }
    )
}

private fun displayRealSize(): Map<String, List<Report>> {
    return map(
        { it.displayRealSize },
        {
            it.displayRealSize.run {
                val width0 = if (rotation == 0 || rotation == 2) width else height
                val height0 = if (rotation == 0 || rotation == 2) height else width
                "${width0}x$height0"
            }
        }
    )
}

private fun displaySize(): Map<String, List<Report>> {
    return map(
        { it.displaySize },
        {
            it.displaySize.run {
                val width0 = if (rotation == 0 || rotation == 2) width else height
                val height0 = if (rotation == 0 || rotation == 2) height else width
                "${width0}x$height0"
            }
        }
    )
}

private fun density(): Map<String, List<Report>> {
    return map(
        { it.density.toString() },
        { it.density.toString() }
    )
}

private fun displayRealSizeDp(): Map<String, List<Report>> {
    return map(
        { it.displayRealSizeDp },
        { it.displayRealSizeDp }
    )
}

private fun locale(): Map<String, List<Report>> {
    return map(
        { it.locale },
        { it.locale }
    )
}

private fun abi(): Map<String, List<Report>> {
    return map(
        { it.abi },
        { it.abi }
    )
}

private fun cameraIds(): Map<String, List<Report>> {
    return map(
        { it.cameraHelper.substringAfter("ids:", "").substringBefore("]", "") + "]" },
        { it.cameraHelper.ids.joinToString(",", "[", "]") }
    )
}

private fun cameraBackDeviceId(): Map<String, List<Report>> {
    return map(
        { it.cameraHelper.substringAfter("backDevice:", "").substringBefore(",").substringBefore("}") },
        { it.cameraHelper.backDeviceId ?: "null" }
    )
}

private fun cameraFrontDeviceId(): Map<String, List<Report>> {
    return map(
        { it.cameraHelper.substringAfter("frontDevice:", "").substringBefore(",").substringBefore("}") },
        { it.cameraHelper.frontDeviceId ?: "null" }
    )
}

private fun cameraBackOldPreferredPreviewSizeForVideo(): Map<String, List<Report>> {
    return map(
        { "0.1.0" },
        { it.cameraHelper.devices.getOrNull(0)?.oldPreferredPreviewSizeForVideo?.run { "${width}x$height" } ?: "null" }
    )
}

private fun cameraFrontOldPreferredPreviewSizeForVideo(): Map<String, List<Report>> {
    return map(
        { "0.1.0" },
        { it.cameraHelper.devices.getOrNull(1)?.oldPreferredPreviewSizeForVideo?.run { "${width}x$height" } ?: "null" }
    )
}

private fun cameraBackFirstOldSupportedPictureSize(): Map<String, List<Report>> {
    return map(
        { "0.1.0" },
        { it.cameraHelper.devices.getOrNull(0)?.oldSupportedPictureSizes?.firstOrNull()?.run { "${width}x$height" } ?: "null" }
    )
}

private fun cameraFrontFirstOldSupportedPictureSize(): Map<String, List<Report>> {
    return map(
        { "0.1.0" },
        { it.cameraHelper.devices.getOrNull(1)?.oldSupportedPictureSizes?.firstOrNull()?.run { "${width}x$height" } ?: "null" }
    )
}

private fun cameraBackFirstOldSupportedPreviewSize(): Map<String, List<Report>> {
    return map(
        { "0.1.0" },
        { it.cameraHelper.devices.getOrNull(0)?.oldSupportedPreviewSizes?.firstOrNull()?.run { "${width}x$height" } ?: "null" }
    )
}

private fun cameraFrontFirstOldSupportedPreviewSize(): Map<String, List<Report>> {
    return map(
        { "0.1.0" },
        { it.cameraHelper.devices.getOrNull(1)?.oldSupportedPreviewSizes?.firstOrNull()?.run { "${width}x$height" } ?: "null" }
    )
}

private fun cameraBackFirstOldSupportedVideoSize(): Map<String, List<Report>> {
    return map(
        { "0.1.0" },
        { it.cameraHelper.devices.getOrNull(0)?.oldSupportedVideoSizes?.firstOrNull()?.run { "${width}x$height" } ?: "null" }
    )
}

private fun cameraFrontFirstOldSupportedVideoSize(): Map<String, List<Report>> {
    return map(
        { "0.1.0" },
        { it.cameraHelper.devices.getOrNull(1)?.oldSupportedVideoSizes?.firstOrNull()?.run { "${width}x$height" } ?: "null" }
    )
}

private fun lensFacings(): Map<String, List<Report>> {
    return map(
        {
            val lensFacing0 = it.cameraHelper.substringAfter("oldId:0,lensFacing:", "").substringBefore(",")
            val lensFacing1 = it.cameraHelper.substringAfter("oldId:1,lensFacing:", "").substringBefore(",")
            val lensFacing2 = it.cameraHelper.substringAfter("oldId:2,lensFacing:", "").substringBefore(",")
            val lensFacing3 = it.cameraHelper.substringAfter("oldId:3,lensFacing:", "").substringBefore(",")
            val lensFacing4 = it.cameraHelper.substringAfter("oldId:4,lensFacing:", "").substringBefore(",")
            "$lensFacing0,$lensFacing1,$lensFacing2,$lensFacing3,$lensFacing4"
        },
        {
            val lensFacing0 = it.cameraHelper.devices.getOrNull(0)?.lensFacingString ?: ""
            val lensFacing1 = it.cameraHelper.devices.getOrNull(1)?.lensFacingString ?: ""
            val lensFacing2 = it.cameraHelper.devices.getOrNull(2)?.lensFacingString ?: ""
            val lensFacing3 = it.cameraHelper.devices.getOrNull(3)?.lensFacingString ?: ""
            val lensFacing4 = it.cameraHelper.devices.getOrNull(4)?.lensFacingString ?: ""
            "$lensFacing0,$lensFacing1,$lensFacing2,$lensFacing3,$lensFacing4"
        }
    )
}

fun main() {
    init()

    val version = version()
    val sdk = sdk()
    val manufacturer = manufacturer()
    val model = model()
    val displayRealSize = displayRealSize()
    val displaySize = displaySize()
    val density = density()
    val displayRealSizeDp = displayRealSizeDp()
    val locale = locale()
    val abi = abi()
    val cameraIds = cameraIds()
    val cameraBackDeviceId = cameraBackDeviceId()
    val cameraFrontDeviceId = cameraFrontDeviceId()
    val cameraBackOldPreferredPreviewSizeForVideo = cameraBackOldPreferredPreviewSizeForVideo()
    val cameraFrontOldPreferredPreviewSizeForVideo = cameraFrontOldPreferredPreviewSizeForVideo()
    val cameraBackFirstOldSupportedPictureSize = cameraBackFirstOldSupportedPictureSize()
    val cameraFrontFirstOldSupportedPictureSize = cameraFrontFirstOldSupportedPictureSize()
    val cameraBackFirstOldSupportedPreviewSize = cameraBackFirstOldSupportedPreviewSize()
    val cameraFrontFirstOldSupportedPreviewSize = cameraFrontFirstOldSupportedPreviewSize()
    val cameraBackFirstOldSupportedVideoSize = cameraBackFirstOldSupportedVideoSize()
    val cameraFrontFirstOldSupportedVideoSize = cameraFrontFirstOldSupportedVideoSize()
    val lensFacings = lensFacings()
}
