package com.ebnbin.eb.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.R
import com.ebnbin.eb.dialog.DialogCancel
import com.ebnbin.eb.dialog.SimpleDialogFragment
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.fragment.FragmentHelper
import com.ebnbin.eb.fragment.removeSelf
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.Consts
import com.ebnbin.eb.util.IntentHelper

/**
 * 权限请求 Fragment.
 */
class PermissionFragment : EBFragment(), SimpleDialogFragment.Callback {
    private lateinit var callback: Callback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = attachCallbackNotNull(Callback::class.java)
    }

    /**
     * 权限结果回调.
     */
    interface Callback {
        fun onPermissionsResult(permissions: ArrayList<String>, granted: Boolean, extraData: Bundle)
    }

    //*****************************************************************************************************************

    private lateinit var permissions: ArrayList<String>
    private lateinit var extraData: Bundle

    private var hasRequestInstallPackagesPermission: Boolean = false
    private var hasSystemAlertWindowPermission: Boolean = false
    private lateinit var runtimePermissions: List<String>

    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        permissions = arguments.getStringArrayList("permissions") ?: throw RuntimeException()
        extraData = arguments.getBundle(Consts.KEY_EXTRA_DATA) ?: throw RuntimeException()

        val validPermissions = LinkedHashSet(permissions)
        hasRequestInstallPackagesPermission = validPermissions.remove(Manifest.permission.REQUEST_INSTALL_PACKAGES)
        hasSystemAlertWindowPermission = validPermissions.remove(Manifest.permission.SYSTEM_ALERT_WINDOW)
        this.runtimePermissions = ArrayList(validPermissions)
    }

    //*****************************************************************************************************************

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            checkPermissions()
        }
    }

    //*****************************************************************************************************************

    private fun checkPermissions() {
        checkRequestInstallPackagesPermission(true)
    }

    private fun checkRequestInstallPackagesPermission(firstTime: Boolean) {
        if (BuildHelper.sdk26O() &&
            hasRequestInstallPackagesPermission &&
            !PermissionHelper.isRequestInstallPackagesPermissionGranted()
        ) {
            if (firstTime) {
                requestRequestInstallPackagesPermission()
            } else {
                onPermissionsResult(false)
            }
        } else {
            checkSystemAlertWindowPermission(true)
        }
    }

    private fun checkSystemAlertWindowPermission(firstTime: Boolean) {
        if (hasSystemAlertWindowPermission && !PermissionHelper.isSystemAlertWindowPermissionGranted()) {
            if (firstTime) {
                requestSystemAlertWindowPermission()
            } else {
                onPermissionsResult(false)
            }
        } else {
            checkRuntimePermissions(CheckRuntimePermissions.FIRST_TIME)
        }
    }

    private fun checkRuntimePermissions(checkRuntimePermissions: CheckRuntimePermissions) {
        runtimePermissions.forEach {
            if (PermissionHelper.isRuntimePermissionGranted(it)) return@forEach
            when (checkRuntimePermissions) {
                CheckRuntimePermissions.FIRST_TIME -> {
                    requestRuntimePermissions()
                }
                CheckRuntimePermissions.REQUEST_RESULT -> {
                    if (shouldShowRequestPermissionRationale(it)) {
                        onPermissionsResult(false)
                    } else {
                        requestRuntimePermissionsDeniedForever()
                    }
                }
                CheckRuntimePermissions.REQUEST_RESULT_DENIED_FOREVER -> {
                    onPermissionsResult(false)
                }
            }
            return
        }
        onPermissionsResult(true)
    }

    /**
     * 在什么情况下检查运行时权限.
     */
    private enum class CheckRuntimePermissions {
        /**
         * 第一次检查.
         */
        FIRST_TIME,
        /**
         * 请求权限返回结果后检查.
         */
        REQUEST_RESULT,
        /**
         * 权限被永久拒绝时手动请求权限返回结果后检查.
         */
        REQUEST_RESULT_DENIED_FOREVER
    }

    //*****************************************************************************************************************

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestRequestInstallPackagesPermission() {
        val extraData = bundleOf(
            "action" to Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
            "request_code" to REQUEST_CODE_REQUEST_INSTALL_PACKAGES_PERMISSION
        )
        startSettingsActivity(R.string.eb_permission_request_install_packages, extraData)
    }

    private fun requestSystemAlertWindowPermission() {
        val extraData = bundleOf(
            "action" to Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "request_code" to REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION
        )
        startSettingsActivity(R.string.eb_permission_system_alert_window, extraData)
    }

    private fun requestRuntimePermissions() {
        requestPermissions(runtimePermissions.toTypedArray(), REQUEST_CODE_RUNTIME_PERMISSIONS)
    }

    private fun requestRuntimePermissionsDeniedForever() {
        val extraData = bundleOf(
            "action" to Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            "request_code" to REQUEST_CODE_RUNTIME_PERMISSIONS_DENIED_FOREVER
        )
        startSettingsActivity(R.string.eb_permission_denied_forever, extraData)
    }

    private fun startSettingsActivity(messageStringId: Int, extraData: Bundle) {
        SimpleDialogFragment.start(childFragmentManager, SimpleDialogFragment.Builder(
            message = getString(messageStringId),
            positive = getString(R.string.eb_permission_dialog_positive),
            negative = getString(R.string.eb_permission_dialog_negative),
            dialogCancel = DialogCancel.NOT_CANCELABLE
        ), "permission", extraData)
    }

    override fun onDialogPositive(extraData: Bundle): Boolean {
        val action = extraData.getString("action") ?: throw RuntimeException()
        val requestCode = extraData.getInt("request_code")
        if (!IntentHelper.startSettingsFromFragment(this, action, requestCode)) {
            onPermissionsResult(false)
        }
        return true
    }

    override fun onDialogNegative(extraData: Bundle): Boolean {
        onPermissionsResult(false)
        return true
    }

    override fun onDialogNeutral(extraData: Bundle): Boolean {
        return true
    }

    override fun onDialogDismiss(extraData: Bundle) {
    }

    //*****************************************************************************************************************

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_REQUEST_INSTALL_PACKAGES_PERMISSION -> {
                checkRequestInstallPackagesPermission(false)
            }
            REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION -> {
                checkSystemAlertWindowPermission(false)
            }
            REQUEST_CODE_RUNTIME_PERMISSIONS_DENIED_FOREVER -> {
                checkRuntimePermissions(CheckRuntimePermissions.REQUEST_RESULT_DENIED_FOREVER)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_RUNTIME_PERMISSIONS -> {
                if (grantResults.isEmpty()) {
                    // 正常情况下不应该发生. 忽略即可.
                } else {
                    checkRuntimePermissions(CheckRuntimePermissions.REQUEST_RESULT)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    //*****************************************************************************************************************

    private fun onPermissionsResult(granted: Boolean) {
        if (!granted) AppHelper.toast(requireContext(), R.string.eb_permission_denied)
        callback.onPermissionsResult(permissions, granted, extraData)
        removeSelf()
    }

    //*****************************************************************************************************************

    companion object {
        private const val REQUEST_CODE_REQUEST_INSTALL_PACKAGES_PERMISSION = 0x1
        private const val REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION = 0x2
        private const val REQUEST_CODE_RUNTIME_PERMISSIONS = 0x3
        private const val REQUEST_CODE_RUNTIME_PERMISSIONS_DENIED_FOREVER = 0x4

        fun start(
            fm: FragmentManager,
            permissions: ArrayList<String>,
            extraData: Bundle = Bundle.EMPTY
        ): PermissionFragment {
            return FragmentHelper.add(fm, PermissionFragment::class.java, arguments = bundleOf(
                "permissions" to permissions,
                Consts.KEY_EXTRA_DATA to extraData
            ))
        }
    }
}
