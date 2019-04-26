package com.ebnbin.eb.splash

import android.content.Intent
import android.os.Bundle
import com.ebnbin.eb.app.EBActivity
import com.ebnbin.eb.util.ebApp

class SplashActivity : EBActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivityClass = ebApp.mainActivityClass
        if (mainActivityClass != null) {
            startActivity(Intent(this, ebApp.mainActivityClass))
        }
        finish()
    }
}
