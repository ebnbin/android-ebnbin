package com.ebnbin.eb.app2.crash

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
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.ebnbin.eb.app2.R
import com.ebnbin.eb.app2.databinding.EbCrashActivityBinding
import com.ebnbin.ebapp.EBAppApplication

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
                    it.appendln(EBAppApplication.instance.createReport())
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
