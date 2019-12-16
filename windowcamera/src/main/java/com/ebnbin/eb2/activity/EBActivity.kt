package com.ebnbin.eb2.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ebnbin.eb.extension.openPermissionFragment
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.eb2.fragment.FragmentHelper
import com.ebnbin.eb2.library.Libraries
import com.ebnbin.eb2.util.Consts

/**
 * Base Activity.
 */
open class EBActivity : com.ebnbin.eb.activity.EBActivity(), PermissionFragment.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 直接关闭.
        if (intent?.getBooleanExtra(KEY_FINISH, false) == true) {
            finish()
            return
        }

        initEventBus()
        initArguments(savedInstanceState)
        initTheme()
        initFragment(savedInstanceState)
    }

    override fun onDestroy() {
        disposeEventBus()
        super.onDestroy()
    }

    //*****************************************************************************************************************

    protected open val isEventBusEnabled: Boolean = false

    private fun initEventBus() {
        if (isEventBusEnabled && !Libraries.eventBus.isRegistered(this)) {
            Libraries.eventBus.register(this)
        }
    }

    private fun disposeEventBus() {
        if (isEventBusEnabled && Libraries.eventBus.isRegistered(this)) {
            Libraries.eventBus.unregister(this)
        }
    }

    //*****************************************************************************************************************

    private fun initArguments(savedInstanceState: Bundle?) {
        onInitArguments(savedInstanceState, intent?.extras ?: Bundle())
    }

    @CallSuper
    protected open fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        if (extras.containsKey(KEY_THEME_ID)) {
            themeId = extras.getInt(KEY_THEME_ID, 0)
        }
        if (extras.containsKey(KEY_FRAGMENT_CLASS)) {
            @Suppress("UNCHECKED_CAST")
            fragmentClass = extras.getSerializable(KEY_FRAGMENT_CLASS) as Class<out Fragment>?
        }
        if (extras.containsKey(KEY_FRAGMENT_PERMISSIONS)) {
            fragmentPermissions = extras.getStringArrayList(KEY_FRAGMENT_PERMISSIONS)
        }
    }

    //*****************************************************************************************************************

    private var themeId: Int = 0

    private fun initTheme() {
        if (themeId == 0) return
        setTheme(themeId)
    }

    //*****************************************************************************************************************

    var fragmentClass: Class<out Fragment>? = null
        private set

    var fragmentPermissions: ArrayList<String>? = null
        private set

    private fun initFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) return
        val fragmentClass = fragmentClass ?: return
        val fragmentPermissions = fragmentPermissions
        if (fragmentPermissions == null) {
            FragmentHelper.add(supportFragmentManager, fragmentClass, android.R.id.content)
        } else {
            supportFragmentManager.openPermissionFragment(fragmentPermissions.toTypedArray(), bundleOf(
                Consts.KEY_CALLING_ID to EBActivity::class.java.name,
                KEY_FRAGMENT_CLASS to fragmentClass
            ))
        }
    }

    override fun permissionOnResult(permissions: Array<out String>, granted: Boolean, extraData: Bundle) {
        super.permissionOnResult(permissions, granted, extraData)
        when (extraData.getString(Consts.KEY_CALLING_ID)) {
            EBActivity::class.java.name -> {
                if (granted) {
                    @Suppress("UNCHECKED_CAST")
                    val fragmentClass = extraData.getSerializable(KEY_FRAGMENT_CLASS) as Class<out Fragment>
                    FragmentHelper.add(supportFragmentManager, fragmentClass, android.R.id.content)
                } else {
                    finish()
                }
            }
        }
    }

    //*****************************************************************************************************************

    override fun onBackPressed() {
        if (FragmentHelper.onBackPressed(supportFragmentManager)) return
        super.onBackPressed()
    }

    //*****************************************************************************************************************

    override fun toString(): String {
        val fragment = supportFragmentManager.findFragmentByTag(fragmentClass?.name)
        val fragmentClass = fragmentClass
        val fragmentString = when {
            fragment != null -> ",$fragment"
            fragmentClass != null -> ",${fragmentClass.name}"
            else -> ""
        }
        return "${super.toString()}$fragmentString"
    }

    //*****************************************************************************************************************

    companion object {
        const val KEY_FINISH = "finish"
        const val KEY_THEME_ID = "theme_id"
        const val KEY_FRAGMENT_CLASS = "fragment_class"
        const val KEY_FRAGMENT_PERMISSIONS = "fragment_permissions"
    }
}
