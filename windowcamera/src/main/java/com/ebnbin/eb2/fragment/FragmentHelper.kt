package com.ebnbin.eb2.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.EBApp

object FragmentHelper {
    fun <T : Fragment> instantiate(fm: FragmentManager, fragmentClass: Class<T>, arguments: Bundle? = null): T {
        val fragment = fm.fragmentFactory.instantiate(EBApp.instance.classLoader, fragmentClass.name)
        fragment.arguments = arguments
        return fragmentClass.cast(fragment) as T
    }

    fun <T : Fragment> add(
        fm: FragmentManager,
        fragmentClass: Class<T>,
        containerViewId: Int = 0,
        tag: String = fragmentClass.name,
        arguments: Bundle? = null
    ): T {
        val fragment = instantiate(fm, fragmentClass, arguments)
        fm.beginTransaction().add(containerViewId, fragment, tag).commitAllowingStateLoss()
        return fragment
    }

    fun remove(fm: FragmentManager, tag: String) {
        val fragment = fm.findFragmentByTag(tag) ?: return
        fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
}
