package com.ebnbin.eb.update

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.app.EBFragment
import com.ebnbin.eb.app.FragmentHelper
import com.ebnbin.eb.async.Loading
import com.ebnbin.eb.net.githubapi.model.Update
import com.ebnbin.eb.sharedpreferences.EBSpManager
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.TimeHelper

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
                if (TimeHelper.expired(EBSpManager.eb.request_update_timestamp.value, UPDATE_EXPIRATION)) {
                    asyncHelper.githubGetJson(
                        Update::class.java,
                        "/update.json",
                        null,
                        onSuccess = {
                            EBSpManager.eb.request_update_timestamp.value = System.currentTimeMillis()
                            if (it.hasUpdate()) {
                                UpdateDialogFragment.start(childFragmentManager, it)
                            }
                        }
                    )
                }
            } else {
                asyncHelper.githubGetJson(
                    Update::class.java,
                    "/update.json",
                    Loading.dialogNotCanceledOnTouchOutside(requireContext()),
                    onSuccess = {
                        EBSpManager.eb.request_update_timestamp.value = System.currentTimeMillis()
                        if (it.hasUpdate()) {
                            UpdateDialogFragment.start(childFragmentManager, it)
                        } else {
                            AppHelper.toast(requireContext(), "已是最新版本。")
                        }
                    },
                    onFailure = {
                        AppHelper.toast(requireContext(), "检测更新失败。")
                    })
            }
        }
    }

    companion object {
        private const val UPDATE_EXPIRATION: Long = 24 * 60 * 60 * 1000L

        fun start(fm: FragmentManager, silent: Boolean) {
            FragmentHelper.add(fm, UpdateFragment::class.java, arguments = bundleOf(
                "silent" to silent
            ))
        }
    }
}
