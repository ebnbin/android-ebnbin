package com.ebnbin.windowcamera.util

import android.os.Environment
import com.ebnbin.eb.util.ebApp
import java.io.File

object IOHelper {
    fun getPath(): File {
        return ebApp.getExternalFilesDir(Environment.DIRECTORY_DCIM) ?: throw RuntimeException()
    }
}
