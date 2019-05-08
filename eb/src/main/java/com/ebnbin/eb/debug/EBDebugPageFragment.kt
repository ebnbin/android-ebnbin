package com.ebnbin.eb.debug

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.about.AboutFragment
import com.ebnbin.eb.activity.EBActivity
import com.ebnbin.eb.async.Loading
import com.ebnbin.eb.crash.CrashRuntimeException
import com.ebnbin.eb.update.UpdateFragment
import com.ebnbin.eb.util.AppHelper

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
            EBActivity.startFragmentFromFragment(this, AboutFragment::class.java, AboutFragment.createIntent())
        }

        addDebugItem("Async", "5 秒后完成，可按返回键或点击空白处取消") {
            asyncHelper.task(
                { Thread.sleep(5000L) },
                Loading.dialog(requireContext()),
                onSuccess = {
                    AppHelper.toast(requireContext(), "onSuccess")
                },
                onFailure = {
                    AppHelper.toast(requireContext(), "onFailure")
                }
            )
        }

        addDebugItem("Async", "5 秒后完成，可按返回键取消") {
            asyncHelper.task(
                { Thread.sleep(5000L) },
                Loading.dialogNotCanceledOnTouchOutside(requireContext()),
                onSuccess = {
                    AppHelper.toast(requireContext(), "onSuccess")
                },
                onFailure = {
                    AppHelper.toast(requireContext(), "onFailure")
                }
            )
        }

        addDebugItem("Async", "3 秒后完成，不可取消") {
            asyncHelper.task(
                { Thread.sleep(3000L) },
                Loading.dialogNotCancelable(requireContext()),
                onSuccess = {
                    AppHelper.toast(requireContext(), "onSuccess")
                },
                onFailure = {
                    AppHelper.toast(requireContext(), "onFailure")
                }
            )
        }

        addDebugItem("Async", "3 个任务，分别 4 秒、5 秒、3 秒后完成，都可按返回键或点击空白处取消") {
            asyncHelper.task(
                { Thread.sleep(4000L) },
                Loading.dialog(requireContext()),
                onSuccess = {
                    AppHelper.toast(requireContext(), "onSuccess 4")
                },
                onFailure = {
                    AppHelper.toast(requireContext(), "onFailure 4")
                }
            )
            asyncHelper.task(
                { Thread.sleep(5000L) },
                Loading.dialog(requireContext()),
                onSuccess = {
                    AppHelper.toast(requireContext(), "onSuccess 5")
                },
                onFailure = {
                    AppHelper.toast(requireContext(), "onFailure 5")
                }
            )
            asyncHelper.task(
                { Thread.sleep(3000L) },
                Loading.dialog(requireContext()),
                onSuccess = {
                    AppHelper.toast(requireContext(), "onSuccess 3")
                },
                onFailure = {
                    AppHelper.toast(requireContext(), "onFailure 3")
                }
            )
        }

        addDebugItem("Update", "silent = true") {
            UpdateFragment.start(childFragmentManager, true)
        }

        addDebugItem("Update", "silent = false") {
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
            AppHelper.restartApp()
        }

        addDebugItem("关闭应用") {
            AppHelper.restartApp(true)
        }

        addDebugItem("Crash") {
            throw CrashRuntimeException()
        }
    }
}
