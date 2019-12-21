package com.ebnbin.eb2.preference

import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb2.async.AsyncHelper

/**
 * Base PreferenceFragment.
 */
abstract class EBPreferenceFragment : PreferenceFragmentCompat() {
    override fun onDestroyView() {
        disposeAsyncHelper()
        super.onDestroyView()
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
