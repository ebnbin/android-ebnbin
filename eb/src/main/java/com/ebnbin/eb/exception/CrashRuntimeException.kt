package com.ebnbin.eb.exception

import com.ebnbin.eb.util.TimeHelper

internal class CrashRuntimeException : RuntimeException(TimeHelper.string("yyyy-MM-dd HH:mm:ss:SSS"))
