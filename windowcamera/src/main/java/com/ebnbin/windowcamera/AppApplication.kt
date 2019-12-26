package com.ebnbin.windowcamera

import com.ebnbin.eb.app2.dev.DevFragment
import com.ebnbin.eb2.EBApplication
import com.ebnbin.eb2.debug.EBDebugPageFragment
import com.ebnbin.windowcamera.dev.Report
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

class AppApplication : EBApplication() {
    override fun onCreate() {
        super.onCreate()
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
    }

    override fun createAppReport(): Any? {
        return Report().create()
    }

    override val devFragmentClass: Class<out DevFragment>
        get() = EBDebugPageFragment::class.java
}
