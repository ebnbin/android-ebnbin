package com.ebnbin.eb.crash

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.R
import kotlinx.android.synthetic.main.eb_crash_activity.*

/**
 * 崩溃页面.
 */
class CrashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        if (intent == null) {
            finish()
            return
        }
        val caocConfig = CustomActivityOnCrash.getConfigFromIntent(intent)
        if (caocConfig == null) {
            finish()
            return
        }
        setContentView(R.layout.eb_crash_activity)
        eb_image_view.setOnLongClickListener {
            eb_image_view.setOnLongClickListener(null)
            eb_log_view.visibility = View.VISIBLE
            val log = CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, intent)
            eb_copy_view.setOnClickListener {
                val clipboardManager = getSystemService<ClipboardManager>() ?: return@setOnClickListener
                clipboardManager.primaryClip = ClipData.newPlainText(CrashActivity::class.java.name, log)
                Toast.makeText(this, R.string.eb_crash_copied, Toast.LENGTH_SHORT).show()
            }
            eb_log_text_view.text = log
            true
        }
        eb_close_view.setOnClickListener {
            CustomActivityOnCrash.closeApplication(this, caocConfig)
        }
        eb_restart_view.setOnClickListener {
            CustomActivityOnCrash.restartApplication(this, caocConfig)
        }
        if (BuildConfig.DEBUG) {
            eb_image_view.performLongClick()
        }
    }

    override fun onBackPressed() {
        // 禁用返回键.
//        super.onBackPressed()
    }
}