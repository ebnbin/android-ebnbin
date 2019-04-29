package com.ebnbin.windowcamera.util

import android.os.Environment
import com.ebnbin.eb.util.TimeHelper
import com.ebnbin.eb.util.ebApp
import java.io.File

object IOHelper {
    fun getPath(): File {
        return ebApp.getExternalFilesDir(Environment.DIRECTORY_DCIM) ?: throw RuntimeException()
    }

    fun nextFile(extension: String): File {
        val path = getPath()
        if (!path.exists()) {
            path.mkdirs()
        }
        val fileName = "${TimeHelper.string("yyyy_MM_dd_HH_mm_ss_SSS")}$extension"
        return File(path, fileName)
    }
}
