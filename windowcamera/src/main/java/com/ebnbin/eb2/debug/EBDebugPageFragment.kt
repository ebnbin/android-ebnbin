package com.ebnbin.eb2.debug

import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.dev.DevFragment
import com.ebnbin.eb.fragment.openFragment
import com.ebnbin.eb2.about.AboutFragment
import com.ebnbin.eb2.update.UpdateFragment
import com.ebnbin.eb2.util.AppHelper
import com.ebnbin.eb2.util.IntentHelper

/**
 * Debug EB 页面.
 */
open class EBDebugPageFragment : DevFragment() {
    override fun onAddDevItems() {
        super.onAddDevItems()

        addDevItem("About") {
            openFragment<AboutFragment>()
        }

        addDevItem("异步任务", "5 秒后完成，可按返回键取消") {
//            asyncHelper.task(
//                { Thread.sleep(5000L) },
//                DialogLoading(requireContext(), DialogCancel.NOT_CANCELED_ON_TOUCH_OUTSIDE),
//                onSuccess = {
//                    AppHelper.toast(requireContext(), "onSuccess")
//                },
//                onFailure = {
//                    AppHelper.toast(requireContext(), "onFailure")
//                }
//            )
        }

        addDevItem("更新") {
            UpdateFragment.start(childFragmentManager, false)
        }

        addDevItem("夜间模式", "关闭") {
            AppHelper.setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        addDevItem("夜间模式", "开启") {
            AppHelper.setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        addDevItem("重置偏好", "TODO")

        addDevItem("重启应用") {
            IntentHelper.restartApp()
        }

        addDevItem("关闭应用") {
            IntentHelper.finishApp()
        }
    }
}
