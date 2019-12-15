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
    private val fragmentIsView: Boolean
        get() = intent?.getBooleanExtra(KEY_FRAGMENT_IS_VIEW, true) ?: throw RuntimeException()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(if (fragmentIsView) android.R.id.content else 0, fragmentClass, fragmentArgs, fragmentTag)
                .commitAllowingStateLoss()
        }
    }

    companion object {
        const val KEY_FRAGMENT_CLASS: String = "fragment_class"
        const val KEY_FRAGMENT_ARGS: String = "fragment_args"
        const val KEY_FRAGMENT_TAG: String = "fragment_tag"
        const val KEY_FRAGMENT_IS_VIEW: String = "fragment_is_view"
    }
}
