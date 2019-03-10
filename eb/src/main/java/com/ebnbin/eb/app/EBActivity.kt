package com.ebnbin.eb.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ebnbin.eb.R

/**
 * Base Activity.
 */
abstract class EBActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AppCompat)
    }
}
