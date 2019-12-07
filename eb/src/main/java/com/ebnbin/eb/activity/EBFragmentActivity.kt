package com.ebnbin.eb.activity

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * 添加单个 EBFragment 的 Activity.
 */
open class EBFragmentActivity : EBActivity() {
    private lateinit var fragmentClass: Class<out Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNCHECKED_CAST")
        fragmentClass = intent?.getSerializableExtra(KEY_FRAGMENT_CLASS) as? Class<out Fragment>? ?:
                throw RuntimeException()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragmentClass, null, fragmentClass.name)
                .commit()
        }
    }

    companion object {
        const val KEY_FRAGMENT_CLASS: String = "fragment_class"
    }
}
