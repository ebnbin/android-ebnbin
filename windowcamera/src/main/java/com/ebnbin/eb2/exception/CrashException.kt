package com.ebnbin.eb2.exception

import com.ebnbin.eb2.util.TimeHelper

internal class CrashException : RuntimeException(TimeHelper.string("yyyy-MM-dd HH:mm:ss:SSS"))
