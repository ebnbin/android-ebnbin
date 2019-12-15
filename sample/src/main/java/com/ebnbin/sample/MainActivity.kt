package com.ebnbin.sample

import android.content.Intent
import android.os.Bundle
import com.ebnbin.eb.activity.EBActivity
import com.ebnbin.eb.activity.EBFragmentActivity
import com.ebnbin.eb.dev.EBDevFragment

class MainActivity : EBActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivityForResult(Intent(this, EBFragmentActivity::class.java)
            .putExtra(EBFragmentActivity.KEY_FRAGMENT_CLASS, EBDevFragment::class.java), 0)
    }
}
