package com.ebnbin.eb.dev

import com.ebnbin.eb.BuildConfig

/**
 * Dev 工具类.
 */
object DevUtil {
    /**
     * 是否启用 Dev 模式.
     */
    val dev: Boolean
        get() = BuildConfig.DEBUG
}
