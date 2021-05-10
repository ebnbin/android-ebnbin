package com.ebnbin.eb.dev

import com.ebnbin.eb.BuildConfig

val dev: Boolean
    get() = BuildConfig.DEBUG
