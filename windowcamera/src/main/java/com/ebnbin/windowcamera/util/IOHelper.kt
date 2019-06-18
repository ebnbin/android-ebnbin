package com.ebnbin.windowcamera.util

import android.os.Environment
import com.ebnbin.eb.util.TimeHelper
import com.ebnbin.eb.util.ebApp
import java.io.File

object IOHelper {
    fun getPath(): File? {
        val result = ebApp.getExternalFilesDir(Environment.DIRECTORY_DCIM) ?: return null
        if (!result.exists()) {
            result.mkdirs()
        }
        return result
    }

    fun nextFile(extension: String): File? {
        val path = getPath() ?: return null
        val fileName = "${TimeHelper.string("yyyy_MM_dd_HH_mm_ss_SSS")}$extension"
        return File(path, fileName)
    }

    var files: List<File> = ArrayList()
        private set

    fun refreshFiles() {
        files = getPath()
            ?.listFiles { _, name ->
                when (name.substringAfterLast(".")) {
                    "jpg", "mp4", "3gp" -> true
                    else -> false
                }
            }
            ?.toSortedSet(Comparator { file1, file2 ->
                compareValuesBy(file2, file1, File::lastModified, File::getName)
            })
            ?.toList()
            ?: ArrayList()
    }
}
