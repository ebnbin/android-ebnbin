package com.ebnbin.eb2.activity

import android.os.Bundle
import com.ebnbin.eb2.fragment.FragmentHelper
import com.ebnbin.eb2.library.Libraries

/**
 * Base Activity.
 */
open class EBActivity : com.ebnbin.eb.activity.EBActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 直接关闭.
        if (intent?.getBooleanExtra(KEY_FINISH, false) == true) {
            finish()
            return
        }

        initEventBus()
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

    override fun onBackPressed() {
        if (FragmentHelper.onBackPressed(supportFragmentManager)) return
        super.onBackPressed()
    }

    //*****************************************************************************************************************

    companion object {
        const val KEY_FINISH = "finish"
    }
}
