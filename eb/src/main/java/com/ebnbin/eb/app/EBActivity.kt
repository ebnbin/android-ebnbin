package com.ebnbin.eb.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ebnbin.eb.debug.DebugSwipeDetector
import com.ebnbin.eb.util.Consts
import com.ebnbin.eb.util.LibraryHelper

/**
 * Base Activity.
 */
open class EBActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.getBooleanExtra(Consts.FINISH, false) == true) {
            finish()
            return
        }

        if (isEventBusEnabled && !LibraryHelper.eventBus.isRegistered(this)) {
            LibraryHelper.eventBus.register(this)
        }

        onInitArguments(savedInstanceState, intent?.extras ?: Bundle.EMPTY)
        initTheme()
        initFragment(savedInstanceState)
    }

    override fun onDestroy() {
        if (isEventBusEnabled && LibraryHelper.eventBus.isRegistered(this)) {
            LibraryHelper.eventBus.unregister(this)
        }
        super.onDestroy()
    }

    //*****************************************************************************************************************

    /**
     * 是否注册 EventBus.
     */
    protected open val isEventBusEnabled: Boolean = false

    //*****************************************************************************************************************

    @CallSuper
    protected open fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        themeStyleId = extras.getInt(KEY_THEME_STYLE_ID)
        @Suppress("UNCHECKED_CAST")
        fragmentClass = extras.getSerializable(KEY_FRAGMENT_CLASS) as Class<out Fragment>?
    }

    //*****************************************************************************************************************

    private var themeStyleId: Int = 0

    private fun initTheme() {
        if (themeStyleId != 0) setTheme(themeStyleId)
    }

    //*****************************************************************************************************************

    var fragmentClass: Class<out Fragment>? = null
        protected set

    private fun initFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) return
        val fragmentClass = fragmentClass ?: return
        FragmentHelper.add(supportFragmentManager, fragmentClass, android.R.id.content)
    }

    //*****************************************************************************************************************

    private val debugSwipeDetector: DebugSwipeDetector by lazy {
        DebugSwipeDetector(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (debugSwipeDetector.dispatchTouchEvent(ev)) return true
        return super.dispatchTouchEvent(ev)
    }

    //*****************************************************************************************************************

    override fun onBackPressed() {
        if (FragmentHelper.onBackPressed(supportFragmentManager)) return
        super.onBackPressed()
    }

    companion object {
        const val KEY_THEME_STYLE_ID = "theme_style_id"

        private const val KEY_FRAGMENT_CLASS = "fragment_class"

        /**
         * 从非 Activity 非 Fragment 启动包装 Fragment 的 EBActivity.
         */
        fun startFragment(
            context: Context,
            fragmentClass: Class<out Fragment>,
            intent: Intent = Intent(),
            options: Bundle? = null
        ) {
            fillStartFragmentIntent(context, fragmentClass, intent)
            context.startActivity(intent, options)
        }

        /**
         * 从 Activity 带 requestCode 启动包装 Fragment 的 EBActivity.
         */
        fun startFragmentFromActivity(
            activity: Activity,
            fragmentClass: Class<out Fragment>,
            intent: Intent = Intent(),
            requestCode: Int = 0,
            options: Bundle? = null
        ) {
            fillStartFragmentIntent(activity, fragmentClass, intent)
            activity.startActivityForResult(intent, requestCode, options)
        }

        /**
         * 从 Fragment 带 requestCode 启动包装 Fragment 的 EBActivity.
         */
        fun startFragmentFromFragment(
            fragment: Fragment,
            fragmentClass: Class<out Fragment>,
            intent: Intent = Intent(),
            requestCode: Int = 0,
            options: Bundle? = null
        ) {
            fillStartFragmentIntent(fragment.requireContext(), fragmentClass, intent)
            fragment.startActivityForResult(intent, requestCode, options)
        }

        private fun fillStartFragmentIntent(context: Context, fragmentClass: Class<out Fragment>, intent: Intent) {
            intent.setClass(context, EBActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClass)
        }
    }
}
