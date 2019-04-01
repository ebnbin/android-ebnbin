package com.ebnbin.eb.app

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.library.eventBus

abstract class EBPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isEventBusEnabled && !eventBus.isRegistered(this)) {
            eventBus.register(this)
        }
    }

    override fun onDestroy() {
        if (isEventBusEnabled && eventBus.isRegistered(this)) {
            eventBus.unregister(this)
        }
        super.onDestroy()
    }

    //*****************************************************************************************************************

    /**
     * 是否注册 EventBus.
     */
    protected open val isEventBusEnabled: Boolean = false
}
