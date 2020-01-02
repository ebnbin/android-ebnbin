package com.ebnbin.eb.crash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.R
import com.ebnbin.eb.databinding.EbCrashActivityBinding

internal class CrashActivity : AppCompatActivity() {
    private val viewModel: CrashViewModel by viewModels()

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
        binding = DataBindingUtil.setContentView(this, R.layout.eb_crash_activity)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.icon = applicationInfo.loadIcon(packageManager)
        binding.setIconOnClick {
            binding.ebBug.rotation = 0f
            binding.ebBug.animate().cancel()
            binding.ebBug.animate()
                .rotationBy(-10f)
                .setInterpolator(DecelerateInterpolator())
                .setDuration(50L)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        binding.ebBug.animate()
                            .rotationBy(20f)
                            .setInterpolator(AccelerateDecelerateInterpolator())
                            .setDuration(100L)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    super.onAnimationEnd(animation)
                                    binding.ebBug.animate()
                                        .rotationBy(-10f)
                                        .setInterpolator(AccelerateInterpolator())
                                        .setDuration(50L)
                                        .setListener(null)
                                        .start()
                                }
                            })
                            .start()
                    }
                })
                .start()
        }
        binding.label = applicationInfo.loadLabel(packageManager).toString()
        binding.setCopyOnClick {
            val clipboardManager = getSystemService<ClipboardManager>() ?: return@setCopyOnClick
            val log = viewModel.log.value ?: return@setCopyOnClick
            clipboardManager.setPrimaryClip(ClipData.newPlainText(CrashActivity::class.java.name, log))
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
                    it.appendln(CustomActivityOnCrash.getStackTraceFromIntent(intent))
                    it.appendln()
                    it.appendln(EBApplication.instance.createReport())
                }
            }.getOrNull()
        }
    }

    override fun onBackPressed() {
        // 禁用返回键.
//        super.onBackPressed()
    }
}
