package com.ebnbin.eb.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.util.ebApp

/**
 * Fragment 帮助类.
 */
object FragmentHelper {
    fun <T : Fragment> instantiate(
        fm: FragmentManager,
        fragmentClass: Class<T>,
        fillArguments: (Bundle.() -> Unit)? = null
    ): T {
        val arguments = if (fillArguments == null) null else Bundle().also { fillArguments(it) }
        val fragment = fm.fragmentFactory.instantiate(ebApp.classLoader, fragmentClass.name, arguments)
        return fragmentClass.cast(fragment) as T
    }

    fun <T : Fragment> add(
        fm: FragmentManager,
        fragmentClass: Class<T>,
        containerViewId: Int = 0,
        tag: String = fragmentClass.name,
        fillArguments: (Bundle.() -> Unit)? = null
    ): T {
        val fragment = instantiate(fm, fragmentClass, fillArguments)
        fm.beginTransaction().add(containerViewId, fragment, tag).commit()
        return fragment
    }
}
