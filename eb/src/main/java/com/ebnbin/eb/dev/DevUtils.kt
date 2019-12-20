package com.ebnbin.eb.dev

import com.ebnbin.eb.BuildConfig

/**
 * 是否启用 Dev 模式.
 */
val dev: Boolean
    get() = BuildConfig.DEBUG
