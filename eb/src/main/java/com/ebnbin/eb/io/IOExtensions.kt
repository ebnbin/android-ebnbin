package com.ebnbin.eb.io

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

suspend fun InputStream.copyTo(
    os: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    listener: ((Long) -> Unit)? = null
): Long {
    var bytesCopied = 0L
    val buffer = buffer(bufferSize)
    var bytes = withContext(Dispatchers.IO) {
        read(buffer)
    }
    while (bytes >= 0) {
        withContext(Dispatchers.IO) {
            os.write(buffer, 0, bytes)
        }
        bytesCopied += bytes
        listener?.invoke(bytesCopied)
        withContext(Dispatchers.IO) {
            bytes = read(buffer)
        }
    }
    return bytesCopied
}
