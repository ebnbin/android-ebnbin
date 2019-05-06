package com.ebnbin.eb.app

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.ebnbin.eb.async.AsyncHelper
import com.ebnbin.eb.util.LibraryHelper

/**
 * Base Fragment.
 */
abstract class EBFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isEventBusEnabled && !LibraryHelper.eventBus.isRegistered(this)) {
            LibraryHelper.eventBus.register(this)
        }

        onInitArguments(savedInstanceState, arguments ?: Bundle.EMPTY, activity?.intent?.extras ?: Bundle.EMPTY)
    }

    override fun onDestroy() {
        if (isEventBusEnabled && LibraryHelper.eventBus.isRegistered(this)) {
            LibraryHelper.eventBus.unregister(this)
        }
        super.onDestroy()
    }

    //*****************************************************************************************************************

    /**
     * 将父 Fragment 或 Activity 强转为 [callbackClass].
     */
    protected fun <T> attachCallback(callbackClass: Class<T>): T? {
        arrayOf(parentFragment, activity).forEach {
            if (callbackClass.isInstance(it)) {
                return callbackClass.cast(it)
            }
        }
        return null
    }

    protected fun <T> attachCallbackNotNull(callbackClass: Class<T>): T {
        return attachCallback(callbackClass) ?: throw RuntimeException()
    }

    //*****************************************************************************************************************

    /**
     * 是否注册 EventBus.
     */
    protected open val isEventBusEnabled: Boolean = false

    //*****************************************************************************************************************

    @CallSuper
    protected open fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
    }

    //*****************************************************************************************************************

    protected val asyncHelper: AsyncHelper = AsyncHelper()

    override fun onDestroyView() {
        asyncHelper.clear()
        super.onDestroyView()
    }

    companion object {
        /**
         * 将自己从 FragmentManager 移除.
         */
        fun Fragment.removeSelf() {
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }
}
