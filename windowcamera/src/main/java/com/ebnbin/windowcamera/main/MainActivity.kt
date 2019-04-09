package com.ebnbin.windowcamera.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ebnbin.eb.app.EBActivity

class MainActivity : EBActivity() {
    override fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        super.onInitArguments(savedInstanceState, extras)
        fragmentClass = MainFragment::class.java
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
