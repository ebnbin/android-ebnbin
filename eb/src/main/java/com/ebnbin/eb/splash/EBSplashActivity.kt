package com.ebnbin.eb.splash

import android.content.Intent
import android.os.Bundle
import com.ebnbin.eb.app.EBActivity

/**
 * 闪屏页面.
 */
abstract class EBSplashActivity : EBActivity() {
    private var review: Boolean = false

    override fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        super.onInitArguments(savedInstanceState, extras)
        review = extras.getBoolean("review", false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (review) {
            val decorView = window?.decorView
            if (decorView == null) {
                // 理论上不应该发生.
                startMainActivity()
            } else {
                decorView.setOnTouchListener { v, _ ->
                    v.setOnTouchListener(null)
                    startMainActivity()
                    true
                }
            }
        } else {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, mainActivityClass))
        finish()
    }

    abstract val mainActivityClass: Class<out EBActivity>

    override fun onBackPressed() {
        // 禁用返回键.
//        super.onBackPressed()
    }
}
