package com.ebnbin.eb.fragment

import androidx.fragment.app.Fragment

/**
 * 基础 Fragment.
 */
abstract class EBFragment : Fragment() {
    /**
     * 将 parentFragment 或 activity 强转为 callback 接口.
     */
    protected fun <T> attachCallback(callbackClass: Class<T>): T? {
        arrayOf(parentFragment, activity).forEach {
            if (callbackClass.isInstance(it)) {
                return callbackClass.cast(it)
            }
        }
        return null
    }
}
