package com.ebnbin.eb.dev

import android.app.Application
import androidx.preference.PreferenceFragmentCompat

object EBDev {
    internal lateinit var app: Application
        private set

    internal var addDevItems: (PreferenceFragmentCompat.() -> Unit)? = null

    fun init(app: Application, addDevItems: (PreferenceFragmentCompat.() -> Unit)? = null) {
        this.app = app
        this.addDevItems = addDevItems

        this.app.registerActivityLifecycleCallbacks(DevFloatingActivityLifecycleCallbacks)
    }
}
