package com.ebnbin.eb.activity

import android.os.Bundle
import android.view.MotionEvent
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ebnbin.eb.debug.DebugSwipeDetector
import com.ebnbin.eb.fragment.FragmentHelper
import com.ebnbin.eb.library.Libraries

/**
 * Base Activity.
 */
open class EBActivity : AppCompatActivity() {
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

    private fun initFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) return
        val fragmentClass = fragmentClass ?: return
        FragmentHelper.add(supportFragmentManager, fragmentClass, android.R.id.content)
    }

    //*****************************************************************************************************************

    override fun onBackPressed() {
        if (FragmentHelper.onBackPressed(supportFragmentManager)) return
        super.onBackPressed()
    }

    //*****************************************************************************************************************

    @Suppress("LeakingThis")
    private val debugSwipeDetector: DebugSwipeDetector = DebugSwipeDetector(this)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (debugSwipeDetector.dispatchTouchEvent(ev)) return true
        return super.dispatchTouchEvent(ev)
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
    }
}
