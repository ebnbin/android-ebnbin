package com.ebnbin.eb.debug

import android.content.Intent
import android.os.Bundle
import com.ebnbin.eb.activity.EBActivity

class DebugActivity : EBActivity() {
    override fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        super.onInitArguments(savedInstanceState, extras)
        fragmentClass = DebugFragment::class.java
    }

    companion object {
        fun start(ebActivity: EBActivity) {
            val intent = Intent(ebActivity, DebugActivity::class.java)
            intent.putExtra(DebugFragment.KEY_CALLING, ebActivity.toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            ebActivity.startActivityForResult(intent, 0)
        }
    }
}
