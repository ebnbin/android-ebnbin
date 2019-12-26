package com.ebnbin.eb.app2.dev

import com.ebnbin.eb.BuildConfig

/**
 * 是否启用 Dev 模式.
 */
val dev: Boolean
    get() = BuildConfig.DEBUG
