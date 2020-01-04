package com.ebnbin.windowcamera.album2

import android.Manifest
import androidx.fragment.app.Fragment
import com.ebnbin.eb.fragment.FragmentActivity

class AlbumActivity : FragmentActivity() {
    override val fragmentClass: Class<out Fragment>
        get() = AlbumFragment::class.java
    override val fragmentPermissions: Array<out String>?
        get() = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
}
