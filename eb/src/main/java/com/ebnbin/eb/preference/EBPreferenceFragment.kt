package com.ebnbin.eb.preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.util.LibraryHelper

abstract class EBPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isEventBusEnabled && !LibraryHelper.eventBus.isRegistered(this)) {
            LibraryHelper.eventBus.register(this)
        }
    }

    override fun onDestroy() {
        if (isEventBusEnabled && LibraryHelper.eventBus.isRegistered(this)) {
            LibraryHelper.eventBus.unregister(this)
        }
        super.onDestroy()
    }

    //*****************************************************************************************************************

    /**
     * 是否注册 EventBus.
     */
    protected open val isEventBusEnabled: Boolean = false
}
