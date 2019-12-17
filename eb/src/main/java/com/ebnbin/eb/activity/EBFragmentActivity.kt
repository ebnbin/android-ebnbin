package com.ebnbin.eb.activity

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ebnbin.eb.extension.hasPermissions
import com.ebnbin.eb.extension.openPermissionFragment
import com.ebnbin.eb.permission.PermissionFragment

/**
 * 添加单个 Fragment 的 Activity.
 */
open class EBFragmentActivity : EBActivity(), PermissionFragment.Callback {
    @Suppress("UNCHECKED_CAST")
    protected open val fragmentClass: Class<out Fragment>
        get() = intent?.getSerializableExtra(KEY_FRAGMENT_CLASS) as? Class<out Fragment>? ?: throw RuntimeException()
    protected open val fragmentArgs: Bundle?
        get() = intent?.getBundleExtra(KEY_FRAGMENT_ARGS)
    protected open val fragmentTag: String?
        get() = intent?.getStringExtra(KEY_FRAGMENT_TAG)
    protected open val fragmentIsView: Boolean
        get() = intent?.getBooleanExtra(KEY_FRAGMENT_IS_VIEW, true) ?: throw RuntimeException()
    protected open val fragmentPermissions: Array<out String>?
        get() = intent?.getStringArrayExtra(KEY_FRAGMENT_PERMISSIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val fragmentPermissions = fragmentPermissions
            if (fragmentPermissions == null || hasPermissions(fragmentPermissions)) {
                addFragment()
            } else {
                supportFragmentManager.openPermissionFragment(fragmentPermissions, bundleOf(
                    KEY_CALLING_ID to EBFragmentActivity::class.java.name
                ))
            }
        }
    }

    override fun permissionOnResult(permissions: Array<out String>, granted: Boolean, extraData: Bundle) {
        super.permissionOnResult(permissions, granted, extraData)
        when (extraData.getString(KEY_CALLING_ID)) {
            EBFragmentActivity::class.java.name -> {
                if (granted) {
                    addFragment()
                } else {
                    finish()
                }
            }
        }
    }

    private fun addFragment() {
        supportFragmentManager.beginTransaction()
            .add(if (fragmentIsView) android.R.id.content else 0, fragmentClass, fragmentArgs, fragmentTag)
            .commitAllowingStateLoss()
    }

    override fun toString(): String {
        val fragmentClass = fragmentClass
        val fragment = supportFragmentManager.fragments.firstOrNull { fragmentClass.isInstance(it) }
        return "${super.toString()}${System.lineSeparator()}${fragment ?: fragmentClass}"
    }

    companion object {
        private const val KEY_CALLING_ID: String = "calling_id"

        const val KEY_FRAGMENT_CLASS: String = "fragment_class"
        const val KEY_FRAGMENT_ARGS: String = "fragment_args"
        const val KEY_FRAGMENT_TAG: String = "fragment_tag"
        const val KEY_FRAGMENT_IS_VIEW: String = "fragment_is_view"
        const val KEY_FRAGMENT_PERMISSIONS: String = "fragment_permissions"
    }
}
