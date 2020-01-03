package com.ebnbin.windowcamera

import com.ebnbin.ebapp.EBAppApplication
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

class AppApplication : EBAppApplication() {
    override fun onCreate() {
        super.onCreate()
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
    }
}
