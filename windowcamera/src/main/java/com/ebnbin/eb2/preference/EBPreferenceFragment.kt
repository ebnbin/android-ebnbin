package com.ebnbin.eb2.preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb2.async.AsyncHelper
import com.ebnbin.eb2.library.Libraries

/**
 * Base PreferenceFragment.
 */
abstract class EBPreferenceFragment : PreferenceFragmentCompat() {
    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initEventBus()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        disposeAsyncHelper()
        disposeEventBus()
        super.onDestroyView()
    }

    //*****************************************************************************************************************

    protected open val isEventBusEnabled: Boolean = false

    private fun initEventBus() {
        if (isEventBusEnabled && !Libraries.eventBus.isRegistered(this)) {
            Libraries.eventBus.register(this)
        }
    }

    private fun disposeEventBus() {
        if (isEventBusEnabled && Libraries.eventBus.isRegistered(this)) {
            Libraries.eventBus.unregister(this)
        }
    }

    //*****************************************************************************************************************

    protected val asyncHelper: AsyncHelper = AsyncHelper()

    private fun disposeAsyncHelper() {
        asyncHelper.clear()
    }

    //*****************************************************************************************************************

    protected fun finish() {
        activity?.finish()
    }
}
