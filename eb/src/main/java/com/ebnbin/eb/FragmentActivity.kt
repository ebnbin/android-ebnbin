package com.ebnbin.eb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

/**
 * 添加单个 Fragment 的 Activity.
 *
 * 需要在 application module 的 AndroidManifest.xml 中注册.
 */
open class FragmentActivity : AppCompatActivity(), PermissionFragment.Callback {
    protected open val fragmentClass: Class<out Fragment>
        get() = requireExtra(KEY_FRAGMENT_CLASS)
    protected open val fragmentArguments: Bundle?
        get() = getExtra(KEY_FRAGMENT_ARGUMENTS)
    protected open val fragmentTag: String?
        get() = getExtra(KEY_FRAGMENT_TAG)
    protected open val fragmentIsView: Boolean
        get() = getExtraOrDefault(KEY_FRAGMENT_IS_VIEW, true)
    protected open val fragmentPermissions: Array<out String>?
        get() = getExtra(KEY_FRAGMENT_PERMISSIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val fragmentPermissions = fragmentPermissions
            if (fragmentPermissions == null || hasPermissions(*fragmentPermissions)) {
                onFragmentPermissionsGranted()
            } else {
                supportFragmentManager.openPermissionFragment(
                    permissions = fragmentPermissions,
                    fragmentCallback = FragmentCallback.ACTIVITY,
                    callbackBundle = bundleOf(
                        "calling_id" to FragmentActivity::class.java.name
                    ),
                    fragmentTag = FragmentActivity::class.java.name
                )
            }
        }
    }

    override fun onPermissionResult(
        context: Context,
        result: PermissionFragment.Result,
        deniedPermission: String?,
        callbackBundle: Bundle
    ): CharSequence? {
        when (callbackBundle.requireValue<String>("calling_id")) {
            FragmentActivity::class.java.name -> {
                if (result == PermissionFragment.Result.GRANTED) {
                    onFragmentPermissionsGranted()
                } else {
                    finish()
                }
            }
        }
        return super.onPermissionResult(context, result, deniedPermission, callbackBundle)
    }

    private fun onFragmentPermissionsGranted() {
        supportFragmentManager.commit(true) {
            add(if (fragmentIsView) android.R.id.content else 0, fragmentClass, fragmentArguments, fragmentTag)
        }
    }

    override fun toString(): String {
        // TODO 混淆.
        val fragment = supportFragmentManager.fragments.firstOrNull { fragmentClass.isInstance(it) }
        return "${super.toString()},${fragment ?: fragmentClass}"
    }

    companion object {
        const val KEY_FRAGMENT_CLASS: String = "fragment_class"
        const val KEY_FRAGMENT_ARGUMENTS: String = "fragment_arguments"
        const val KEY_FRAGMENT_TAG: String = "fragment_tag"
        const val KEY_FRAGMENT_IS_VIEW: String = "fragment_is_view"
        const val KEY_FRAGMENT_PERMISSIONS: String = "fragment_permissions"

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
