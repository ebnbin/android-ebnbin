package com.ebnbin.eb.debug

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.loading.Loading
import com.ebnbin.eb.net.NetHelper
import com.ebnbin.eb.sharedpreferences.EBSp
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

        addDebugItem("Update") {
            asyncRequest(NetHelper.ebService.update(),
                Loading.DIALOG,
                onNext = { toast(requireContext(), "${it.version}") },
                onError = { toast(requireContext(), it) }
            )
        }

        addDebugItem("Loading Dialog", "（可取消）") {
            showLoadingDialog(true)
        }

        addDebugItem("Loading Dialog", "（不可取消，3 秒后自动关闭）") {
            showLoadingDialog(false)
            asyncTask(
                {
                    Thread.sleep(3000L)
                },
                onNext = {
                    hideLoadingDialog()
                },
                onError = {
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
