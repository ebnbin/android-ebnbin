package com.ebnbin.eb2.crash

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.ebnbin.windowcamera.BuildConfig
import com.ebnbin.windowcamera.R
import com.ebnbin.eb2.dev.EBReport
import kotlinx.android.synthetic.main.eb_crash_activity.*

/**
 * 崩溃页面.
 */
internal class CrashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        if (intent == null) {
            finish()
            return
        }
        val caocConfig = try {
            CustomActivityOnCrash.getConfigFromIntent(intent)
        } catch (e: Exception) {
            null
        }
        if (caocConfig == null) {
            finish()
            return
        }
        setContentView(R.layout.eb_crash_activity)
        eb_icon.setOnLongClickListener {
            eb_icon.setOnLongClickListener(null)
            eb_log_view.visibility = View.VISIBLE
            val log = getLog(intent)
            eb_copy_view.setOnClickListener {
                val clipboardManager = getSystemService<ClipboardManager>() ?: return@setOnClickListener
                clipboardManager.setPrimaryClip(ClipData.newPlainText(CrashActivity::class.java.name, log))
                Toast.makeText(this, R.string.eb_crash_copied, Toast.LENGTH_SHORT).show()
            }
            eb_log_text_view.text = log
            true
        }
        eb_title_text_view.text = getString(R.string.eb_crash_title, getString(R.string.eb_label))
        eb_close_view.setOnClickListener {
            CustomActivityOnCrash.closeApplication(this, caocConfig)
        }
        eb_restart_view.setOnClickListener {
            CustomActivityOnCrash.restartApplication(this, caocConfig)
        }
        if (BuildConfig.DEBUG) {
            eb_icon.performLongClick()
        }
    }

    private fun getLog(intent: Intent): CharSequence {
        val ebReport = try {
            EBReport().create().toString()
        } catch (throwable: Throwable) {
            "ERROR"
        }
        return "$ebReport\n\nStack trace:\n${CustomActivityOnCrash.getStackTraceFromIntent(intent)}"
    }

    override fun onBackPressed() {
        // 禁用返回键.
//        super.onBackPressed()
    }
}
