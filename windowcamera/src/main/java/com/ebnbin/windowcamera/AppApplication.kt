package com.ebnbin.windowcamera

import com.ebnbin.eb.dev.EBDevFragment
import com.ebnbin.eb2.EBApplication
import com.ebnbin.windowcamera.debug.DebugPageFragment
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

class AppApplication : EBApplication() {
    override fun onCreate() {
        super.onCreate()
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
    }

    override val devFragmentClass: Class<out EBDevFragment>
        get() = DebugPageFragment::class.java
}
