package com.ebnbin.eb2.activity

import android.os.Bundle
import com.ebnbin.eb.app2.activity.EBActivity

/**
 * Base Activity.
 */
open class EBActivity : EBActivity() {
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
