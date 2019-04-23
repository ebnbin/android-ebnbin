package com.ebnbin.eb.update

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.app.EBFragment
import com.ebnbin.eb.app.FragmentHelper
import com.ebnbin.eb.async.Loading
import com.ebnbin.eb.net.NetHelper
import com.ebnbin.eb.sharedpreferences.EBSp
import com.ebnbin.eb.util.toast

class UpdateFragment : EBFragment() {
    private var silent: Boolean = false

    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        silent = arguments.getBoolean("silent", false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            if (silent) {
                if (System.currentTimeMillis() - EBSp.eb.request_update_timestamp.value >= UPDATE_INTERVAL) {
                    asyncHelper.request(NetHelper.ebService.update(),
                        onSuccess = {
                            EBSp.eb.request_update_timestamp.value = System.currentTimeMillis()
                            if (it.hasUpdate()) {
                                UpdateDialogFragment.start(childFragmentManager, it)
                            }
                        }
                    )
                }
            } else {
                asyncHelper.request(NetHelper.ebService.update(),
                    Loading.DIALOG_NOT_CANCELED_ON_TOUCH_OUTSIDE,
                    onSuccess = {
                        EBSp.eb.request_update_timestamp.value = System.currentTimeMillis()
                        if (it.hasUpdate()) {
                            UpdateDialogFragment.start(childFragmentManager, it)
                        } else {
                            toast(requireContext(), "已是最新版本。")
                        }
                    },
                    onFailure = {
                        toast(requireContext(), "检测更新失败。")
                    })
            }
        }
    }

    companion object {
        private const val UPDATE_INTERVAL: Long = 24 * 60 * 60 * 1000L

        fun start(fm: FragmentManager, silent: Boolean) {
            FragmentHelper.add(fm, UpdateFragment::class.java, arguments = bundleOf(
                "silent" to silent
            ))
        }
    }
}
