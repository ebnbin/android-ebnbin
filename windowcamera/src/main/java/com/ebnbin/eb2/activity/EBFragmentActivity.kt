package com.ebnbin.eb2.activity

import com.ebnbin.eb2.fragment.FragmentHelper

/**
 * Base Activity.
 */
open class EBFragmentActivity : com.ebnbin.eb.activity.EBFragmentActivity() {
    override fun onBackPressed() {
        if (FragmentHelper.onBackPressed(supportFragmentManager)) return
        super.onBackPressed()
    }
}
