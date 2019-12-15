package com.ebnbin.eb.activity

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * 添加单个 Fragment 的 Activity.
 */
open class EBFragmentActivity : EBActivity() {
    @Suppress("UNCHECKED_CAST")
    private val fragmentClass: Class<out Fragment>
        get() = intent?.getSerializableExtra(KEY_FRAGMENT_CLASS) as? Class<out Fragment>? ?: throw RuntimeException()
    private val fragmentArgs: Bundle?
        get() = intent?.getBundleExtra(KEY_FRAGMENT_ARGS)
    private val fragmentTag: String?
        get() = intent?.getStringExtra(KEY_FRAGMENT_TAG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragmentClass, fragmentArgs, fragmentTag)
                .commitAllowingStateLoss()
        }
    }

    companion object {
        const val KEY_FRAGMENT_CLASS: String = "fragment_class"
        const val KEY_FRAGMENT_ARGS: String = "fragment_args"
        const val KEY_FRAGMENT_TAG: String = "fragment_tag"
    }
}
