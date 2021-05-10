package com.ebnbin.eb.net

import com.ebnbin.eb.io.copyTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

suspend fun ResponseBody.download(listener: DownloadListener) {
    val contentType = contentType()
    val contentLength = contentLength()
    val file = listener.onStart(contentType, contentLength)
    val fos = file.outputStream()
    val `is` = byteStream()
    val length = `is`.copyTo(fos) {
        listener.onProgress(it.toFloat() / contentLength, it, contentLength)
    }
    withContext(Dispatchers.IO) {
        `is`.close()
        fos.close()
    }
    if (length == contentLength) {
        listener.onSuccess()
    } else {
        listener.onFailure()
    }
}
