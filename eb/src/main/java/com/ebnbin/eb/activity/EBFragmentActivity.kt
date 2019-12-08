package com.ebnbin.eb.activity

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * 添加单个 EBFragment 的 Activity.
 */
open class EBFragmentActivity : EBActivity() {
    lateinit var fragmentClass: Class<out Fragment>
        private set
    private var fragmentArguments: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNCHECKED_CAST")
        fragmentClass = intent?.getSerializableExtra(KEY_FRAGMENT_CLASS) as? Class<out Fragment>? ?:
                throw RuntimeException()
        fragmentArguments = intent?.getBundleExtra(KEY_FRAGMENT_ARGUMENTS)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragmentClass, fragmentArguments, fragmentClass.name)
                .commit()
        }
    }

    companion object {
        const val KEY_FRAGMENT_CLASS: String = "fragment_class"
        const val KEY_FRAGMENT_ARGUMENTS: String = "fragment_arguments"
    }
}
