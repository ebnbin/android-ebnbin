package com.ebnbin.windowcamera

import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb2.sharedpreferences.EBSpManager
import com.ebnbin.ebapp.EBAppApplication
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

class AppApplication : EBAppApplication() {
    override fun onCreate() {
        super.onCreate()
        initNightMode()
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
    }

    private fun initNightMode() {
        AppCompatDelegate.setDefaultNightMode(EBSpManager.night_mode.value)
    }
}
