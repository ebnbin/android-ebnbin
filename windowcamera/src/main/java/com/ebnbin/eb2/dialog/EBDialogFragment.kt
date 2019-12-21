package com.ebnbin.eb2.dialog

import android.os.Bundle
import androidx.annotation.CallSuper
import com.ebnbin.eb2.async.AsyncHelper

/**
 * Base DialogFragment.
 */
abstract class EBDialogFragment : com.ebnbin.eb.dialog.EBDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArguments(savedInstanceState)
    }

    override fun onDestroyView() {
        disposeAsyncHelper()
        super.onDestroyView()
    }

    //*****************************************************************************************************************

    private fun initArguments(savedInstanceState: Bundle?) {
        onInitArguments(savedInstanceState, arguments ?: Bundle(), activity?.intent?.extras ?: Bundle())
    }

    @CallSuper
    protected open fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
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
