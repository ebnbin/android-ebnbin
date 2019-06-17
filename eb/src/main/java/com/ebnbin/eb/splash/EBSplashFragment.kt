package com.ebnbin.eb.splash

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.core.os.bundleOf
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.R
import com.ebnbin.eb.async.DialogLoading
import com.ebnbin.eb.dev.DevHelper
import com.ebnbin.eb.dev.EBReport
import com.ebnbin.eb.dialog.DialogCancel
import com.ebnbin.eb.dialog.SimpleDialogFragment
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.githubapi.model.content.Update
import com.ebnbin.eb.sharedpreferences.EBSpManager
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.Consts
import com.ebnbin.eb.util.IntentHelper

open class EBSplashFragment : EBFragment(), SimpleDialogFragment.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DevHelper.report { createReport() }
        if (BuildConfig.FLAVOR == "google" || BuildHelper.isSignatureValid()) {
            onInit(savedInstanceState)
        } else {
            asyncHelper.githubGetJson(
                Update::class.java,
                "/update.json",
                DialogLoading(requireContext(), DialogCancel.NOT_CANCELABLE),
                onSuccess = {
                    SimpleDialogFragment.start(childFragmentManager, SimpleDialogFragment.Builder(
                        message = getString(R.string.eb_splash_signature_message),
                        positive = getString(R.string.eb_splash_signature_positive),
                        negative = getString(R.string.eb_splash_signature_negative),
                        dialogCancel = DialogCancel.NOT_CANCELABLE
                    ), "splash_signature", bundleOf(
                        Consts.KEY_CALLING_ID to "splash_signature",
                        "url" to it.url
                    ))
                },
                onFailure = {
                    AppHelper.toast(requireContext(), R.string.eb_splash_signature_toast)
                    finish()
                }
            )
        }
    }

    @CallSuper
    protected open fun onInit(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val oldVersion = EBSpManager.version.value
            val newVersion = BuildHelper.versionCode
            if (oldVersion < newVersion) {
                EBSpManager.version.value = newVersion
                onNewVersion(oldVersion, newVersion)
            }
        }
    }

    protected open fun createReport(): EBReport {
        return EBReport().create()
    }

    @CallSuper
    protected open fun onNewVersion(oldVersion: Int, newVersion: Int) {
        EBSpManager.last_update_timestamp.value = 0L
        EBSpManager.last_report_timestamp.value = 0L
    }

    @CallSuper
    override fun onDialogPositive(extraData: Bundle): Boolean {
        when (extraData.getString(Consts.KEY_CALLING_ID)) {
            "splash_signature" -> {
                val url = extraData.getString("url") ?: return false
                IntentHelper.startBrowser(requireContext(), url)
                AppHelper.copy(url)
                AppHelper.toast(requireContext(), R.string.eb_splash_signature_copied)
                return false
            }
            else -> return true
        }
    }

    @CallSuper
    override fun onDialogNegative(extraData: Bundle): Boolean {
        when (extraData.getString(Consts.KEY_CALLING_ID)) {
            "splash_signature" -> {
                finish()
                return false
            }
            else -> return true
        }
    }

    @CallSuper
    override fun onDialogNeutral(extraData: Bundle): Boolean {
        return true
    }

    @CallSuper
    override fun onDialogDismiss(extraData: Bundle) {
    }
}
