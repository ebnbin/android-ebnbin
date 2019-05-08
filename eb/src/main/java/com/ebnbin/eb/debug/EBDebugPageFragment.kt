package com.ebnbin.eb.debug

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.about.AboutFragment
import com.ebnbin.eb.async.DialogLoading
import com.ebnbin.eb.crash.CrashRuntimeException
import com.ebnbin.eb.dialog.DialogCancel
import com.ebnbin.eb.update.UpdateFragment
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.IntentHelper

/**
 * Debug EB 页面.
 */
internal class EBDebugPageFragment : BaseDebugPageFragment() {
    private var calling: String? = null

    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        calling = activityExtras.getString(DebugFragment.KEY_CALLING)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDebugItem("Calling", calling.toString())

        addDebugItem("About") {
            IntentHelper.startFragmentFromFragment(this, AboutFragment.createIntent())
        }

        addDebugItem("异步任务", "5 秒后完成，可按返回键取消") {
            asyncHelper.task(
                { Thread.sleep(5000L) },
                DialogLoading(requireContext(), DialogCancel.NOT_CANCELED_ON_TOUCH_OUTSIDE),
                onSuccess = {
                    AppHelper.toast(requireContext(), "onSuccess")
                },
                onFailure = {
                    AppHelper.toast(requireContext(), "onFailure")
                }
            )
        }

        addDebugItem("更新") {
            UpdateFragment.start(childFragmentManager, false)
        }

        addDebugItem("夜间模式", "关闭") {
            AppHelper.setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        addDebugItem("夜间模式", "开启") {
            AppHelper.setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        addDebugItem("重置偏好", "TODO") {
            // 删除 sp 后重启到 MainActivity, 在 Application 中保存的偏好不会被初始化 (如: version_code), 需要在 MainActivity
            // 中处理.
        }

        addDebugItem("重启应用") {
            IntentHelper.restartApp()
        }

        addDebugItem("关闭应用") {
            IntentHelper.finishApp()
        }

        addDebugItem("Crash") {
            throw CrashRuntimeException()
        }
    }
}
