package com.ebnbin.eb.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.util.ebApp

/**
 * Fragment 帮助类.
 */
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

    fun onBackPressed(fm: FragmentManager): Boolean {
        val topFragment = fm.fragments
            .reversed()
            .firstOrNull()
        if (topFragment is EBFragment) {
            return topFragment.onBackPressed()
        }
        return false
    }
}
