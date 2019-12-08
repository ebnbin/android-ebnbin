package com.ebnbin.sample

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ebnbin.eb.activity.EBActivity
import com.ebnbin.eb.debug.DebugHelper
import com.jeremyliao.liveeventbus.LiveEventBus

class MainActivity : EBActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LiveEventBus.get(DebugHelper.KEY_LEFT_TO_RIGHT)
            .observe(this, Observer {
                Toast.makeText(this, "debug", Toast.LENGTH_SHORT).show()
            })
    }
}
