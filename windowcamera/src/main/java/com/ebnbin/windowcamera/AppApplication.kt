package com.ebnbin.windowcamera

import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb.app2.dev.DevFragment2
import com.ebnbin.eb.dev.EBDev
import com.ebnbin.eb2.debug.EBDebugPageFragment
import com.ebnbin.eb2.sharedpreferences.EBSpManager
import com.ebnbin.windowcamera.dev.Report
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

class AppApplication : EBApp() {
    override fun onCreate() {
        super.onCreate()
        initNightMode()
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)

        EBDev.init(this)
    }

    private fun initNightMode() {
        AppCompatDelegate.setDefaultNightMode(EBSpManager.night_mode.value)
    }

    override fun createAppReport(): Any? {
        return Report().create()
    }

    override val devFragmentClass: Class<out DevFragment2>
        get() = EBDebugPageFragment::class.java
}
