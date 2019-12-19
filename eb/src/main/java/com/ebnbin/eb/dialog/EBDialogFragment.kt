package com.ebnbin.eb.dialog

import androidx.appcompat.app.AppCompatDialogFragment
import com.ebnbin.eb.fragment.FragmentCallbackCast

/**
 * 基础 DialogFragment.
 */
abstract class EBDialogFragment : AppCompatDialogFragment() {
    /**
     * 将 parentFragment 或 activity 强转为 callback 接口.
     *
     * @param fragmentCallbackCast 按照什么顺序强转 Callback 接口.
     */
    protected inline fun <reified T> getCallback(
        fragmentCallbackCast: FragmentCallbackCast = FragmentCallbackCast.PREFER_PARENT_FRAGMENT
    ): T? {
        when (fragmentCallbackCast) {
            FragmentCallbackCast.PREFER_PARENT_FRAGMENT -> arrayOf(parentFragment, activity)
            FragmentCallbackCast.PREFER_ACTIVITY -> arrayOf(activity, parentFragment)
            FragmentCallbackCast.PARENT_FRAGMENT -> arrayOf(parentFragment)
            FragmentCallbackCast.ACTIVITY -> arrayOf(activity)
        }.forEach {
            if (T::class.java.isInstance(it)) {
                return T::class.java.cast(it)
            }
        }
        return null
    }

    /**
     * 将 parentFragment 或 activity 强转为 callback 接口. 如果强转失败则抛出异常.
     */
    protected inline fun <reified T> requireCallback(
        fragmentCallbackCast: FragmentCallbackCast = FragmentCallbackCast.PREFER_PARENT_FRAGMENT
    ): T {
        return getCallback<T>(fragmentCallbackCast)!!
    }

    //*****************************************************************************************************************

    protected fun containsArgument(key: String): Boolean {
        return arguments?.containsKey(key) ?: false
    }

    protected inline fun <reified T> getArgument(key: String): T? {
        return arguments?.get(key) as T?
    }

    /**
     * 如果获取失败则返回默认值.
     */
    protected inline fun <reified T> getArgumentOrDefault(key: String, defaultValue: T): T {
        return getArgument<T>(key) ?: defaultValue
    }

    /**
     * 如果获取失败则抛出异常.
     */
    protected inline fun <reified T> requireArgument(key: String): T {
        return getArgument<T>(key)!!
    }
}
