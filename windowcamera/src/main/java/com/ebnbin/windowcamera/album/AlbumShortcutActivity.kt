package com.ebnbin.windowcamera.album

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.ebnbin.eb2.activity.EBActivity
import com.ebnbin.windowcamera.R

class AlbumShortcutActivity : EBActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.action == Intent.ACTION_CREATE_SHORTCUT) {
            val shortcut = ShortcutInfoCompat.Builder(this, "shortcut_album")
                .setIntent(Intent(this, AlbumActivity::class.java).setAction(Intent.ACTION_MAIN))
                .setIcon(IconCompat.createWithResource(this, R.mipmap.shortcut_album))
                .setShortLabel(getString(R.string.album))
                .build()
            setResult(Activity.RESULT_OK, ShortcutManagerCompat.createShortcutResultIntent(this, shortcut))
        } else {
            setResult(Activity.RESULT_CANCELED)
        }
        finish()
    }
}
