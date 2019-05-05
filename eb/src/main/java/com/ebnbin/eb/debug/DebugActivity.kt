package com.ebnbin.eb.debug

import android.content.Intent
import android.os.Bundle
import com.ebnbin.eb.R
import com.ebnbin.eb.app.EBActivity

class DebugActivity : EBActivity() {
    override fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        super.onInitArguments(savedInstanceState, extras)
        themeStyleId = R.style.EBTheme_Debug
        fragmentClass = DebugFragment::class.java
    }

    companion object {
        fun start(ebActivity: EBActivity) {
            val intent = Intent(ebActivity, DebugActivity::class.java)
            intent.putExtra(DebugFragment.KEY_CALLING_FRAGMENT_CLASS_NAME, ebActivity.fragmentClass?.name)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            ebActivity.startActivityForResult(intent, 0)
        }
    }
}
