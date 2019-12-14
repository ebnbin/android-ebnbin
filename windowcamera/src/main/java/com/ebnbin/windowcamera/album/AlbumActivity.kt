package com.ebnbin.windowcamera.album

import android.Manifest
import android.os.Bundle
import com.ebnbin.eb2.activity.EBActivity

class AlbumActivity : EBActivity() {
    override fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        extras.putSerializable(KEY_FRAGMENT_CLASS, AlbumFragment::class.java)
        extras.putStringArrayList(KEY_FRAGMENT_PERMISSIONS,
            arrayListOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        super.onInitArguments(savedInstanceState, extras)
    }
}
