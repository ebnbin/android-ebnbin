package com.ebnbin.eb

import android.app.Application
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.dev.DevFloatingActivityLifecycleCallbacks
import com.ebnbin.eb.dev.EBDevPageFragment

open class EBApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initDev()
    }

    private fun initDev() {
        registerActivityLifecycleCallbacks(DevFloatingActivityLifecycleCallbacks)
    }

    open val devPages: List<Pair<Class<out PreferenceFragmentCompat>, CharSequence>>
        get() = listOf(EBDevPageFragment::class.java to "EB")

    companion object {
        lateinit var instance: EBApplication
            private set
    }
}
