package com.ebnbin.eb2.activity

import android.os.Bundle

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
    }

    //*****************************************************************************************************************

    companion object {
        const val KEY_FINISH = "finish"
    }
}
