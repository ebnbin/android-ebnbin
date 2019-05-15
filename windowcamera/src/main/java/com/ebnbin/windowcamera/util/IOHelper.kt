package com.ebnbin.windowcamera.util

import android.os.Environment
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.TimeHelper
import java.io.File

object IOHelper {
    fun getPath(): File {
        val result = File(Environment.getExternalStorageDirectory(), BuildHelper.applicationId)
        if (!result.exists()) {
            result.mkdirs()
        }
        return result
    }

    fun nextFile(extension: String): File {
        val path = getPath()
        val fileName = "${TimeHelper.string("yyyy_MM_dd_HH_mm_ss_SSS")}$extension"
        return File(path, fileName)
    }
}
