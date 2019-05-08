package com.ebnbin.eb.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.util.ebApp

object FragmentHelper {
    fun <T : Fragment> instantiate(fm: FragmentManager, fragmentClass: Class<T>): T {
        val fragment = fm.fragmentFactory.instantiate(ebApp.classLoader, fragmentClass.name)
        return fragmentClass.cast(fragment) as T
    }

    fun <T : Fragment> add(
        fm: FragmentManager,
        fragmentClass: Class<T>,
        containerViewId: Int = 0,
        tag: String = fragmentClass.name,
        arguments: Bundle? = null
    ): T {
        val fragment = instantiate(fm, fragmentClass)
        fragment.arguments = arguments
        fm.beginTransaction().add(containerViewId, fragment, tag).commit()
        return fragment
    }

    /**
     * 需要在 EBActivity 和 EBFragment 的 onBackPressed 中调用.
     *
     * @return 是否已经处理了 back 事件.
     */
    internal fun onBackPressed(fm: FragmentManager): Boolean {
        return (fm.fragments.lastOrNull() as? EBFragment?)?.onBackPressed() == true
    }
}
