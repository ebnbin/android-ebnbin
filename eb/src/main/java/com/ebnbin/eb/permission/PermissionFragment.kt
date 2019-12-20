package com.ebnbin.eb.permission

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import com.ebnbin.eb.R
import com.ebnbin.eb.activity.openActivity
import com.ebnbin.eb.dialog.AlertDialogFragment
import com.ebnbin.eb.dialog.DialogCancelable
import com.ebnbin.eb.dialog.openAlertDialog
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.fragment.getArgumentOrDefault
import com.ebnbin.eb.fragment.getCallback
import com.ebnbin.eb.fragment.removeSelf
import com.ebnbin.eb.fragment.requireArgument
import com.ebnbin.eb.util.KEY_CALLBACK_BUNDLE
import com.ebnbin.eb.util.KEY_CALLING_ID
import com.ebnbin.eb.util.applicationId
import com.ebnbin.eb.util.requireValue
import com.ebnbin.eb.util.sdk26O
import com.ebnbin.eb.widget.toast

/**
 * 权限请求 Fragment.
 */
class PermissionFragment : EBFragment(), AlertDialogFragment.Callback {
    /**
     * 权限结果回调.
     */
    interface Callback {
        fun onPermissionResult(permissions: Array<out String>, granted: Boolean, callbackBundle: Bundle) = Unit
    }

    //*****************************************************************************************************************

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArguments()
        if (savedInstanceState == null) {
            checkPermissions()
        }
    }

    //*****************************************************************************************************************

    private var needRequestInstallPackagesPermission: Boolean = false
    private var needSystemAlertWindowPermission: Boolean = false
    private var needWriteSettingsPermission: Boolean = false
    private lateinit var runtimePermissions: Array<out String>

    private fun initArguments() {
        val permissionSet = LinkedHashSet(requireArgument<Array<out String>>(KEY_PERMISSIONS).toList())
        needRequestInstallPackagesPermission = permissionSet.remove(Manifest.permission.REQUEST_INSTALL_PACKAGES)
        needSystemAlertWindowPermission = permissionSet.remove(Manifest.permission.SYSTEM_ALERT_WINDOW)
        needWriteSettingsPermission = permissionSet.remove(Manifest.permission.WRITE_SETTINGS)
        runtimePermissions = permissionSet.toTypedArray()
    }

    //*****************************************************************************************************************

    private fun checkPermissions() {
        checkRequestInstallPackagesPermission(true)
    }

    private fun checkRequestInstallPackagesPermission(firstTime: Boolean) {
        if (sdk26O() &&
            needRequestInstallPackagesPermission &&
            !requireContext().hasRequestInstallPackagesPermission()) {
            if (firstTime) {
                requestRequestInstallPackagesPermission()
            } else {
                onPermissionResult(PermissionResult.DENIED)
            }
        } else {
            checkSystemAlertWindowPermission(true)
        }
    }

    private fun checkSystemAlertWindowPermission(firstTime: Boolean) {
        if (needSystemAlertWindowPermission && !requireContext().hasSystemAlertWindowPermission()) {
            if (firstTime) {
                requestSystemAlertWindowPermission()
            } else {
                onPermissionResult(PermissionResult.DENIED)
            }
        } else {
            checkWriteSettingsPermission(true)
        }
    }

    private fun checkWriteSettingsPermission(firstTime: Boolean) {
        if (needWriteSettingsPermission && !requireContext().hasWriteSettingsPermission()) {
            if (firstTime) {
                requestWriteSettingsPermission()
            } else {
                onPermissionResult(PermissionResult.DENIED)
            }
        } else {
            checkRuntimePermissions(CheckRuntimePermissions.FIRST_TIME)
        }
    }

    private fun checkRuntimePermissions(checkRuntimePermissions: CheckRuntimePermissions) {
        runtimePermissions.forEach {
            if (requireContext().hasRuntimePermission(it)) return@forEach
            when (checkRuntimePermissions) {
                CheckRuntimePermissions.FIRST_TIME -> {
                    requestRuntimePermissions()
                }
                CheckRuntimePermissions.REQUEST_RESULT -> {
                    if (shouldShowRequestPermissionRationale(it)) {
                        onPermissionResult(PermissionResult.DENIED)
                    } else {
                        requestRuntimePermissionsDeniedForever()
                    }
                }
                CheckRuntimePermissions.REQUEST_RESULT_DENIED_FOREVER -> {
                    onPermissionResult(PermissionResult.DENIED)
                }
            }
            return
        }
        onPermissionResult(PermissionResult.GRANTED)
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
        openDialog(
            R.string.eb_permission_request_install_packages,
            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
            REQUEST_CODE_REQUEST_INSTALL_PACKAGES_PERMISSION
        )
    }

    private fun requestSystemAlertWindowPermission() {
        openDialog(
            R.string.eb_permission_system_alert_window,
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION
        )
    }

    private fun requestWriteSettingsPermission() {
        openDialog(
            R.string.eb_permission_write_settings,
            Settings.ACTION_MANAGE_WRITE_SETTINGS,
            REQUEST_CODE_WRITE_SETTINGS_PERMISSION
        )
    }

    private fun requestRuntimePermissions() {
        requestPermissions(runtimePermissions, REQUEST_CODE_RUNTIME_PERMISSIONS)
    }

    private fun requestRuntimePermissionsDeniedForever() {
        openDialog(
            R.string.eb_permission_runtime_permissions_denied_forever,
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            REQUEST_CODE_RUNTIME_PERMISSIONS_DENIED_FOREVER
        )
    }

    //*****************************************************************************************************************

    private fun openDialog(@StringRes messageId: Int, action: String, requestCode: Int) {
        childFragmentManager.openAlertDialog(
            message = getString(messageId),
            positiveText = getString(R.string.eb_permission_open_settings),
            negativeText = getString(R.string.eb_permission_deny),
            dialogCancelable = DialogCancelable.NOT_CANCELABLE,
            callbackBundle = bundleOf(
                KEY_CALLING_ID to PermissionFragment::class.java.name,
                "action" to action,
                "request_code" to requestCode
            ),
            fragmentTag = PermissionFragment::class.java.name
        )
    }

    override fun onAlertDialogPositive(alertDialog: AlertDialog, callbackBundle: Bundle): Boolean {
        return when (callbackBundle.requireValue<String>(KEY_CALLING_ID)) {
            PermissionFragment::class.java.name -> {
                val intent = Intent(callbackBundle.requireValue("action"), Uri.parse("package:$applicationId"))
                if (openActivity(intent, callbackBundle.requireValue("request_code")) != null) {
                    onPermissionResult(PermissionResult.OPEN_SETTINGS_FAILURE)
                }
                true
            }
            else -> super.onAlertDialogPositive(alertDialog, callbackBundle)
        }
    }

    override fun onAlertDialogNegative(alertDialog: AlertDialog, callbackBundle: Bundle): Boolean {
        onPermissionResult(PermissionResult.DENIED)
        return true
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
            REQUEST_CODE_WRITE_SETTINGS_PERMISSION -> {
                checkWriteSettingsPermission(false)
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
                checkRuntimePermissions(CheckRuntimePermissions.REQUEST_RESULT)
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    //*****************************************************************************************************************

    private enum class PermissionResult {
        /**
         * 权限全部允许.
         */
        GRANTED,
        /**
         * 权限被拒绝.
         */
        DENIED,
        /**
         * 打开系统设置页面失败.
         */
        OPEN_SETTINGS_FAILURE
    }

    private fun onPermissionResult(permissionResult: PermissionResult) {
        val callback = getCallback<Callback>()
        val permissions = requireArgument<Array<out String>>(KEY_PERMISSIONS)
        val callbackBundle = getArgumentOrDefault(KEY_CALLBACK_BUNDLE, Bundle.EMPTY)
        when (permissionResult) {
            PermissionResult.GRANTED -> {
                callback?.onPermissionResult(permissions, true, callbackBundle)
            }
            PermissionResult.DENIED -> {
                requireContext().toast(R.string.eb_permission_denied)
                callback?.onPermissionResult(permissions, false, callbackBundle)
            }
            PermissionResult.OPEN_SETTINGS_FAILURE -> {
                requireContext().toast(R.string.eb_permission_open_settings_failure)
                callback?.onPermissionResult(permissions, false, callbackBundle)
            }
        }
        removeSelf()
    }

    //*****************************************************************************************************************

    companion object {
        private const val REQUEST_CODE_REQUEST_INSTALL_PACKAGES_PERMISSION = 0x1
        private const val REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION = 0x2
        private const val REQUEST_CODE_WRITE_SETTINGS_PERMISSION = 0x3
        private const val REQUEST_CODE_RUNTIME_PERMISSIONS = 0x4
        private const val REQUEST_CODE_RUNTIME_PERMISSIONS_DENIED_FOREVER = 0x5

        private const val KEY_PERMISSIONS: String = "permissions"

        fun createArguments(permissions: Array<out String>, callbackBundle: Bundle = Bundle.EMPTY): Bundle {
            return bundleOf(
                KEY_PERMISSIONS to permissions,
                KEY_CALLBACK_BUNDLE to callbackBundle
            )
        }
    }
}
