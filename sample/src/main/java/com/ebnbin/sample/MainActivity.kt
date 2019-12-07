package com.ebnbin.sample

import android.os.Bundle
import com.ebnbin.eb.activity.EBActivity
import com.ebnbin.eb.ebApp
import com.ebnbin.eb.extension.startFragmentByActivity

class MainActivity : EBActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startFragmentByActivity(ebApp.debugFragmentClass)
    }
}
