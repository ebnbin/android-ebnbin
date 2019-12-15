package com.ebnbin.eb.dialog

import androidx.appcompat.app.AppCompatDialogFragment

/**
 * 基础 DialogFragment.
 */
abstract class EBDialogFragment : AppCompatDialogFragment() {
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
