package com.ebnbin.eb

import android.util.Log

/**
 * @param any 如果为 [Throwable] 则输出异常栈.
 *
 * @param chunkedSize Log 分段输出.
 *
 * @param debug 如果为 false 则不输出 log.
 */
fun log(
    vararg any: Any?,
    priority: Int = Log.DEBUG,
    tag: String = "ebnbin",
    chunkedSize: Int = 3840,
    debug: Boolean = BuildConfig.DEBUG
) {
    if (!debug) return
    any
        .joinToString(lineSeparator) {
            if (it is Throwable) Log.getStackTraceString(it) else it.toString()
        }
        .chunked(chunkedSize)
        .forEach {
            Log.println(priority, tag, it)
        }
}
