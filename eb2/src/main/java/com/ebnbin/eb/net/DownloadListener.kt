package com.ebnbin.eb.net

import okhttp3.MediaType
import java.io.File

interface DownloadListener {
    fun onStart(contentType: MediaType?, contentLength: Long): File

    fun onProgress(percent: Float, currentLength: Long, totalLength: Long) = Unit

    fun onSuccess() = Unit

    fun onFailure() = Unit
}
