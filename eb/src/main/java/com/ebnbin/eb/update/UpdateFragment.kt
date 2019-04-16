package com.ebnbin.eb.update

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.app.EBFragment
import com.ebnbin.eb.app.FragmentHelper
import com.ebnbin.eb.loading.Loading
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
                if (System.currentTimeMillis() - EBSp.eb.request_update_timestamp >= UPDATE_INTERVAL) {
                    asyncRequest(NetHelper.ebService.update(),
                        onNext = {
                            EBSp.eb.request_update_timestamp = System.currentTimeMillis()
                            if (it.hasUpdate()) {
                                UpdateDialogFragment.start(childFragmentManager, it)
                            }
                        }
                    )
                }
            } else {
                asyncRequest(NetHelper.ebService.update(),
                    Loading.DIALOG,
                    onNext = {
                        EBSp.eb.request_update_timestamp = System.currentTimeMillis()
                        if (it.hasUpdate()) {
                            UpdateDialogFragment.start(childFragmentManager, it)
                        } else {
                            toast(requireContext(), "已是最新版本。")
                        }
                    },
                    onError = {
                        toast(requireContext(), "检测更新失败。")
                    })
            }
        }
    }

    companion object {
        private const val UPDATE_INTERVAL: Long = 24 * 60 * 60 * 1000L

        fun start(fm: FragmentManager, silent: Boolean) {
            FragmentHelper.add(fm, UpdateFragment::class.java) {
                putBoolean("silent", silent)
            }
        }
    }
}
