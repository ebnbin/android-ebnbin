package com.ebnbin.eb.debug

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.dialog.Cancel
import com.ebnbin.eb.sharedpreferences.EBSp
import com.ebnbin.eb.update.UpdateFragment
import com.ebnbin.eb.util.restartMainActivity

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

        addDebugItem("Update", "silent = true") {
            UpdateFragment.start(childFragmentManager, true)
        }

        addDebugItem("Update", "silent = false") {
            UpdateFragment.start(childFragmentManager, false)
        }

        addDebugItem("LoadingDialogFragment", "CANCELABLE") {
            showLoadingDialog(Cancel.CANCELABLE)
        }

        addDebugItem("LoadingDialogFragment", "NOT_CANCELED_ON_TOUCH_OUTSIDE") {
            showLoadingDialog(Cancel.NOT_CANCELED_ON_TOUCH_OUTSIDE)
        }

        addDebugItem("LoadingDialogFragment", "NOT_CANCELABLE（3 秒后自动关闭）") {
            showLoadingDialog(Cancel.NOT_CANCELABLE)
            asyncHelper.task(
                {
                    Thread.sleep(3000L)
                },
                onSuccess = {
                    hideLoadingDialog()
                },
                onFailure = {
                    hideLoadingDialog()
                })
        }

        addDebugItem("夜间模式", "关闭") {
            EBSp.eb.night_mode = AppCompatDelegate.MODE_NIGHT_NO
            restartMainActivity()
        }

        addDebugItem("夜间模式", "开启") {
            EBSp.eb.night_mode = AppCompatDelegate.MODE_NIGHT_YES
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
