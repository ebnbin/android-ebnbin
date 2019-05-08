package com.ebnbin.eb.debug

import android.content.Intent
import com.ebnbin.eb.activity.EBActivity
import com.ebnbin.eb.util.IntentHelper

class DebugActivity : EBActivity() {
    companion object {
        fun start(ebActivity: EBActivity) {
            val intent = Intent(ebActivity, DebugActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(KEY_FRAGMENT_CLASS, DebugFragment::class.java)
                .putExtra(DebugFragment.KEY_CALLING, ebActivity.toString())
            IntentHelper.startActivityFromActivity(ebActivity, intent)
        }
    }
}
