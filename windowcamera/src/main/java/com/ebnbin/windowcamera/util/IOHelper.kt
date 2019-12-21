package com.ebnbin.windowcamera.util

import android.os.Environment
import com.ebnbin.eb.EBApp
import com.ebnbin.eb.util.applicationId
import com.ebnbin.eb2.util.TimeHelper
import java.io.File

object IOHelper {
    fun getPath(): File {
        val result = File(Environment.getExternalStorageDirectory(),
            "${EBApp.instance.applicationId}/${Environment.DIRECTORY_DCIM}/")
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

    var files: List<File> = ArrayList()
        private set

    fun refreshFiles() {
        files = getPath()
            .listFiles { _, name ->
                when (name.substringAfterLast(".")) {
                    "jpg", "mp4", "3gp" -> true
                    else -> false
                }
            }
            .toSortedSet(Comparator { file1, file2 ->
                compareValuesBy(file2, file1, File::lastModified, File::getName)
            })
            .toList()
    }
}
