package com.ebnbin.sample

import android.os.Bundle
import com.ebnbin.eb.activity.EBActivity
import com.ebnbin.eb.ebApp

class MainActivity : EBActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, ebApp.debugFragmentClass, null, ebApp.debugFragmentClass.name)
                .commit()
        }
    }
}
