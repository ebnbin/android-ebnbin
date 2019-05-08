package com.ebnbin.eb.preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.library.Libraries

abstract class EBPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isEventBusEnabled && !Libraries.eventBus.isRegistered(this)) {
            Libraries.eventBus.register(this)
        }
    }

    override fun onDestroy() {
        if (isEventBusEnabled && Libraries.eventBus.isRegistered(this)) {
            Libraries.eventBus.unregister(this)
        }
        super.onDestroy()
    }

    //*****************************************************************************************************************

    /**
     * 是否注册 EventBus.
     */
    protected open val isEventBusEnabled: Boolean = false
}
