package com.ebnbin.eb

import android.os.Handler
import android.os.Looper

/**
 * Main looper handler.
 */
val mainHandler: Handler
    get() = Handler(Looper.getMainLooper())
