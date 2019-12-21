package com.ebnbin.eb.crash

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.ebnbin.eb.R
import com.ebnbin.eb.databinding.EbCrashActivityBinding
import com.ebnbin.eb.dev.Report

/**
 * 自定义崩溃页面.
 */
internal class CrashActivity : AppCompatActivity() {
    private val viewModel: CrashActivityViewModel by viewModels()

    private lateinit var binding: EbCrashActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        if (intent == null) {
            finish()
            return
        }
        val caocConfig = runCatching {
            CustomActivityOnCrash.getConfigFromIntent(intent)
        }.getOrNull()
        if (caocConfig == null) {
            finish()
            return
        }
        binding = EbCrashActivityBinding.inflate(
            layoutInflater,
            window.decorView.findViewById(android.R.id.content),
            false
        )
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.setCopyOnClick {
            val clipboardManager = getSystemService<ClipboardManager>() ?: return@setCopyOnClick
            clipboardManager.setPrimaryClip(ClipData.newPlainText(CrashActivity::class.java.name, viewModel.log.value))
            Toast.makeText(this, R.string.eb_crash_copied, Toast.LENGTH_SHORT).show()
        }
        binding.setCloseOnClick {
            runCatching {
                CustomActivityOnCrash.closeApplication(this, caocConfig)
            }
        }
        binding.setRestartOnClick {
            runCatching {
                CustomActivityOnCrash.restartApplication(this, caocConfig)
            }
        }
        if (viewModel.log.value == null) {
            viewModel.log.value = runCatching {
                StringBuilder().also {
                    it.appendln(Report.create(false))
                    it.appendln()
                    it.append(CustomActivityOnCrash.getStackTraceFromIntent(intent))
                }
            }.getOrNull()
        }
    }

    override fun onBackPressed() {
        // 禁用返回键.
//        super.onBackPressed()
    }
}
