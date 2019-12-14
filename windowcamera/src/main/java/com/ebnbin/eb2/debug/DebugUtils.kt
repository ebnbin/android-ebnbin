package com.ebnbin.eb2.debug

import android.util.Log
import com.ebnbin.windowcamera.BuildConfig

/**
 * Debug 模式.
 */
val debug: Boolean
    get() = BuildConfig.DEBUG

/**
 * 在 debug 模式下输出日志.
 *
 * @param any 要输出的对象. 如果为 [Throwable] 则输出异常栈.
 *
 * @param priority 日志级别.
 */
fun log(vararg any: Any?, priority: Int = Log.ERROR, tag: String = "ebnbin") {
    if (!debug) return
    if (any.isEmpty()) return
    val sb = StringBuilder()
    any.forEach {
        sb.appendln(if (it is Throwable) {
            Log.getStackTraceString(it)
        } else {
            it
        })
    }
    sb.chunked(4000).forEach {
        Log.println(priority, tag, it)
    }
}
