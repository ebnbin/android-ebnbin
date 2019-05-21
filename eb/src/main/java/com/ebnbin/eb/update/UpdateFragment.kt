package com.ebnbin.eb.update

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.R
import com.ebnbin.eb.async.DialogLoading
import com.ebnbin.eb.dialog.DialogCancel
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.fragment.FragmentHelper
import com.ebnbin.eb.fragment.removeSelf
import com.ebnbin.eb.githubapi.model.content.Update
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
                if (TimeHelper.expired(EBSpManager.last_update_timestamp.value, UPDATE_EXPIRATION)) {
                    asyncHelper.githubGetJson(
                        Update::class.java,
                        "/update.json",
                        null,
                        onSuccess = {
                            EBSpManager.last_update_timestamp.value = if (it.hasForceUpdate()) 0L else
                                TimeHelper.long()
                            if (it.hasUpdate()) {
                                UpdateDialogFragment.start(requireFragmentManager(), it)
                            }
                            removeSelf()
                        },
                        onFailure = {
                            removeSelf()
                        }
                    )
                }
            } else {
                asyncHelper.githubGetJson(
                    Update::class.java,
                    "/update.json",
                    DialogLoading(requireContext(), DialogCancel.NOT_CANCELED_ON_TOUCH_OUTSIDE),
                    onSuccess = {
                        EBSpManager.last_update_timestamp.value = if (it.hasForceUpdate()) 0L else TimeHelper.long()
                        if (it.hasUpdate()) {
                            UpdateDialogFragment.start(requireFragmentManager(), it)
                        } else {
                            AppHelper.toast(requireContext(), R.string.eb_update_latest)
                        }
                        removeSelf()
                    },
                    onFailure = {
                        AppHelper.toast(requireContext(), R.string.eb_update_failure)
                        removeSelf()
                    }
                )
            }
        }
    }

    companion object {
        private const val UPDATE_EXPIRATION: Long = 24 * 60 * 60 * 1000L

        /**
         * @param silent 静默更新.
         */
        fun start(fm: FragmentManager, silent: Boolean): UpdateFragment {
            return FragmentHelper.add(fm, UpdateFragment::class.java, arguments = bundleOf(
                "silent" to silent
            ))
        }
    }
}
