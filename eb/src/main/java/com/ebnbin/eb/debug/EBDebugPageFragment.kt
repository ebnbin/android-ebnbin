package com.ebnbin.eb.debug

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.async.Loading
import com.ebnbin.eb.sharedpreferences.EBSpManager
import com.ebnbin.eb.update.UpdateFragment
import com.ebnbin.eb.util.restartMainActivity
import com.ebnbin.eb.util.toast

/**
 * Debug EB 页面.
 */
internal class EBDebugPageFragment : BaseDebugPageFragment() {
    private var callingFragmentClassName: String? = null

    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        callingFragmentClassName = activityExtras.getString(DebugFragment.KEY_CALLING_FRAGMENT_CLASS_NAME)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDebugItem("Calling Activity", activity?.callingActivity?.className.toString())

        addDebugItem("Calling Fragment", callingFragmentClassName.toString())

        addDebugItem("Async", "5 秒后完成，可按返回键或点击空白处取消") {
            asyncHelper.task({ Thread.sleep(5000L) }, Loading.DIALOG_CANCELABLE,
                onSuccess = {
                    toast(requireContext(), "onSuccess")
                },
                onFailure = {
                    toast(requireContext(), "onFailure")
                }
            )
        }

        addDebugItem("Async", "5 秒后完成，可按返回键取消") {
            asyncHelper.task({ Thread.sleep(5000L) }, Loading.DIALOG_NOT_CANCELED_ON_TOUCH_OUTSIDE,
                onSuccess = {
                    toast(requireContext(), "onSuccess")
                },
                onFailure = {
                    toast(requireContext(), "onFailure")
                }
            )
        }

        addDebugItem("Async", "3 秒后完成，不可取消") {
            asyncHelper.task({ Thread.sleep(3000L) }, Loading.DIALOG_NOT_CANCELABLE,
                onSuccess = {
                    toast(requireContext(), "onSuccess")
                },
                onFailure = {
                    toast(requireContext(), "onFailure")
                }
            )
        }

        addDebugItem("Async", "3 个任务，分别 4 秒、5 秒、3 秒后完成，都可按返回键或点击空白处取消") {
            asyncHelper.task({ Thread.sleep(4000L) }, Loading.DIALOG_CANCELABLE,
                onSuccess = {
                    toast(requireContext(), "onSuccess 4")
                },
                onFailure = {
                    toast(requireContext(), "onFailure 4")
                }
            )
            asyncHelper.task({ Thread.sleep(5000L) }, Loading.DIALOG_CANCELABLE,
                onSuccess = {
                    toast(requireContext(), "onSuccess 5")
                },
                onFailure = {
                    toast(requireContext(), "onFailure 5")
                }
            )
            asyncHelper.task({ Thread.sleep(3000L) }, Loading.DIALOG_CANCELABLE,
                onSuccess = {
                    toast(requireContext(), "onSuccess 3")
                },
                onFailure = {
                    toast(requireContext(), "onFailure 3")
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
            EBSpManager.eb.night_mode.value = AppCompatDelegate.MODE_NIGHT_NO
            restartMainActivity()
        }

        addDebugItem("夜间模式", "开启") {
            EBSpManager.eb.night_mode.value = AppCompatDelegate.MODE_NIGHT_YES
            restartMainActivity()
        }

        addDebugItem("重置偏好", "TODO") {
            // 删除 sp 后重启到 MainActivity, 在 Application 中保存的偏好不会被初始化 (如: version_code), 需要在 MainActivity
            // 中处理.
        }

        addDebugItem("重启 MainActivity") {
            restartMainActivity()
        }

        addDebugItem("Crash") {
            throw RuntimeException()
        }
    }
}
