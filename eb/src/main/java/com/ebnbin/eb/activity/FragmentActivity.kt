package com.ebnbin.eb.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.eb.permission.hasPermissions
import com.ebnbin.eb.permission.openPermissionFragment
import com.ebnbin.eb.util.KEY_CALLING_ID
import com.ebnbin.eb.util.requireValue

/**
 * 添加单个 Fragment 的 Activity.
 */
open class FragmentActivity : EBActivity(), PermissionFragment.Callback {
    protected open val fragmentClass: Class<out Fragment>
        get() = requireExtra(KEY_FRAGMENT_CLASS)
    protected open val fragmentArguments: Bundle?
        get() = requireExtra(KEY_FRAGMENT_ARGUMENTS)
    protected open val fragmentTag: String?
        get() = requireExtra(KEY_FRAGMENT_TAG)
    protected open val fragmentIsView: Boolean
        get() = requireExtra(KEY_FRAGMENT_IS_VIEW)
    protected open val fragmentPermissions: Array<out String>?
        get() = requireExtra(KEY_FRAGMENT_PERMISSIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val fragmentPermissions = fragmentPermissions
            if (fragmentPermissions == null || hasPermissions(*fragmentPermissions)) {
                onFragmentPermissionsGranted()
            } else {
                supportFragmentManager.openPermissionFragment(
                    permissions = fragmentPermissions,
                    callbackBundle = bundleOf(
                        KEY_CALLING_ID to FragmentActivity::class.java.name
                    ),
                    fragmentTag = FragmentActivity::class.java.name
                )
            }
        }
    }

    override fun onPermissionResult(permissions: Array<out String>, granted: Boolean, callbackBundle: Bundle) {
        when (callbackBundle.requireValue<String>(KEY_CALLING_ID)) {
            FragmentActivity::class.java.name -> {
                if (granted) {
                    onFragmentPermissionsGranted()
                } else {
                    finish()
                }
            }
            else -> super.onPermissionResult(permissions, granted, callbackBundle)
        }
    }

    private fun onFragmentPermissionsGranted() {
        supportFragmentManager.commit(true) {
            add(if (fragmentIsView) android.R.id.content else 0, fragmentClass, fragmentArguments, fragmentTag)
        }
    }

    override fun toString(): String {
        val fragmentClass = fragmentClass
        val fragment = supportFragmentManager.fragments.firstOrNull { fragmentClass.isInstance(it) }
        return "${super.toString()},${fragment ?: fragmentClass}"
    }

    companion object {
        private const val KEY_FRAGMENT_CLASS: String = "fragment_class"
        private const val KEY_FRAGMENT_ARGUMENTS: String = "fragment_arguments"
        private const val KEY_FRAGMENT_TAG: String = "fragment_tag"
        private const val KEY_FRAGMENT_IS_VIEW: String = "fragment_is_view"
        private const val KEY_FRAGMENT_PERMISSIONS: String = "fragment_permissions"

        fun createIntent(
            context: Context,
            fragmentClass: Class<out Fragment>,
            fragmentArguments: Bundle? = null,
            fragmentTag: String? = null,
            fragmentIsView: Boolean = true,
            fragmentPermissions: Array<out String>? = null,
            activityIntent: Intent? = null
        ): Intent {
            return (if (activityIntent == null) Intent() else Intent(activityIntent))
                .setClass(context, FragmentActivity::class.java)
                .putExtra(KEY_FRAGMENT_CLASS, fragmentClass)
                .putExtra(KEY_FRAGMENT_ARGUMENTS, fragmentArguments)
                .putExtra(KEY_FRAGMENT_TAG, fragmentTag)
                .putExtra(KEY_FRAGMENT_IS_VIEW, fragmentIsView)
                .putExtra(KEY_FRAGMENT_PERMISSIONS, fragmentPermissions)
        }
    }
}
