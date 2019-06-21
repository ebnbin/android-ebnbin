package com.ebnbin.windowcamera.album

import android.os.Bundle
import com.ebnbin.eb.activity.EBActivity

class AlbumActivity : EBActivity() {
    override fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        extras.putSerializable(KEY_FRAGMENT_CLASS, AlbumFragment::class.java)
        super.onInitArguments(savedInstanceState, extras)
    }
}
