package com.ebnbin.eb.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import com.ebnbin.eb.R
import com.ebnbin.eb.dialog.AlertDialogFragment
import com.ebnbin.eb.dialog.DialogCancelable
import com.ebnbin.eb.extension.hasRequestInstallPackagesPermission
import com.ebnbin.eb.extension.hasRuntimePermission
import com.ebnbin.eb.extension.hasSystemAlertWindowPermission
import com.ebnbin.eb.extension.openActivity
import com.ebnbin.eb.extension.openAlertDialog
import com.ebnbin.eb.extension.removeSelf
import com.ebnbin.eb.fragment.EBFragment

/**
 * 权限请求 Fragment.
 */
class PermissionFragment : EBFragment(), AlertDialogFragment.Callback {
    private var callback: Callback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = attachCallback(Callback::class.java)
    }

    /**
     * 权限结果回调.
     */
    interface Callback {
        fun permissionOnResult(permissions: Array<out String>, granted: Boolean, extraData: Bundle) = Unit
    }

    //*****************************************************************************************************************

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
        if (savedInstanceState == null) {
            checkPermissions()
        }
    }

    //*****************************************************************************************************************

    private lateinit var permissions: Array<out String>
    private lateinit var extraData: Bundle

    private var needRequestInstallPackagesPermission: Boolean = false
    private var needSystemAlertWindowPermission: Boolean = false
    private lateinit var runtimePermissions: Array<out String>

    private fun initArgs() {
        permissions = arguments?.getStringArray(KEY_PERMISSIONS) ?: throw RuntimeException()
        extraData = arguments?.getBundle(KEY_EXTRA_DATA) ?: throw RuntimeException()

        val permissionSet = LinkedHashSet(permissions.toList())
        needRequestInstallPackagesPermission = permissionSet.remove(Manifest.permission.REQUEST_INSTALL_PACKAGES)
        needSystemAlertWindowPermission = permissionSet.remove(Manifest.permission.SYSTEM_ALERT_WINDOW)
        runtimePermissions = permissionSet.toTypedArray()
    }

    //*****************************************************************************************************************

    private fun checkPermissions() {
        checkRequestInstallPackagesPermission(true)
    }

    private fun checkRequestInstallPackagesPermission(firstTime: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            needRequestInstallPackagesPermission &&
            !hasRequestInstallPackagesPermission()
        ) {
            if (firstTime) {
                requestRequestInstallPackagesPermission()
            } else {
                permissionOnResult(PermissionResult.DENIED)
            }
        } else {
            checkSystemAlertWindowPermission(true)
        }
    }

    private fun checkSystemAlertWindowPermission(firstTime: Boolean) {
        if (needSystemAlertWindowPermission && !hasSystemAlertWindowPermission()) {
            if (firstTime) {
                requestSystemAlertWindowPermission()
            } else {
                permissionOnResult(PermissionResult.DENIED)
            }
        } else {
            checkRuntimePermissions(CheckRuntimePermissions.FIRST_TIME)
        }
    }

    private fun checkRuntimePermissions(checkRuntimePermissions: CheckRuntimePermissions) {
        runtimePermissions.forEach {
            if (hasRuntimePermission(it)) return@forEach
            when (checkRuntimePermissions) {
                CheckRuntimePermissions.FIRST_TIME -> {
                    requestRuntimePermissions()
                }
                CheckRuntimePermissions.REQUEST_RESULT -> {
                    if (shouldShowRequestPermissionRationale(it)) {
                        permissionOnResult(PermissionResult.DENIED)
                    } else {
                        requestRuntimePermissionsDeniedForever()
                    }
                }
                CheckRuntimePermissions.REQUEST_RESULT_DENIED_FOREVER -> {
                    permissionOnResult(PermissionResult.DENIED)
                }
            }
            return
        }
        permissionOnResult(PermissionResult.GRANTED)
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
            R.string.eb_permission_dialog_message_request_install_packages,
            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
            REQUEST_CODE_REQUEST_INSTALL_PACKAGES_PERMISSION
        )
    }

    private fun requestSystemAlertWindowPermission() {
        openDialog(
            R.string.eb_permission_dialog_message_system_alert_window,
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION
        )
    }

    private fun requestRuntimePermissions() {
        requestPermissions(runtimePermissions, REQUEST_CODE_RUNTIME_PERMISSIONS)
    }

    private fun requestRuntimePermissionsDeniedForever() {
        openDialog(
            R.string.eb_permission_dialog_message_runtime_permissions_denied_forever,
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            REQUEST_CODE_RUNTIME_PERMISSIONS_DENIED_FOREVER
        )
    }

    private fun openDialog(@StringRes messageStringId: Int, action: String, requestCode: Int) {
        childFragmentManager.openAlertDialog(AlertDialogFragment.Builder(
            message = getString(messageStringId),
            positiveButtonText = getString(R.string.eb_permission_dialog_positive),
            negativeButtonText = getString(R.string.eb_permission_dialog_negative),
            dialogCancelable = DialogCancelable.NOT_CANCELABLE,
            extraData = bundleOf(
                "action" to action,
                "request_code" to requestCode
            )
        ))
    }

    //*****************************************************************************************************************

    override fun alertDialogOnPositive(alertDialog: AlertDialog, extraData: Bundle): Boolean {
        val action = extraData.getString("action") ?: throw RuntimeException()
        val requestCode = extraData.getInt("request_code")
        val intent = Intent(action, Uri.parse("package:${requireContext().packageName}"))
        if (openActivity(intent, requestCode = requestCode) != null) {
            permissionOnResult(PermissionResult.DENIED_OPEN_SETTINGS_FAILURE)
        }
        return true
    }

    override fun alertDialogOnNegative(alertDialog: AlertDialog, extraData: Bundle): Boolean {
        permissionOnResult(PermissionResult.DENIED)
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
         * 权限被拒绝且打开设置页面失败.
         */
        DENIED_OPEN_SETTINGS_FAILURE
    }

    private fun permissionOnResult(permissionResult: PermissionResult) {
        when (permissionResult) {
            PermissionResult.GRANTED -> {
                callback?.permissionOnResult(permissions, true, extraData)
            }
            PermissionResult.DENIED -> {
                Toast.makeText(requireContext(), R.string.eb_permission_denied, Toast.LENGTH_SHORT).show()
                callback?.permissionOnResult(permissions, false, extraData)
            }
            PermissionResult.DENIED_OPEN_SETTINGS_FAILURE -> {
                Toast.makeText(requireContext(), R.string.eb_permission_denied_open_settings_failure,
                    Toast.LENGTH_SHORT).show()
                callback?.permissionOnResult(permissions, false, extraData)
            }
        }
        removeSelf()
    }

    //*****************************************************************************************************************

    companion object {
        private const val REQUEST_CODE_REQUEST_INSTALL_PACKAGES_PERMISSION = 0x1
        private const val REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION = 0x2
        private const val REQUEST_CODE_RUNTIME_PERMISSIONS = 0x3
        private const val REQUEST_CODE_RUNTIME_PERMISSIONS_DENIED_FOREVER = 0x4

        const val KEY_PERMISSIONS: String = "permissions"
        const val KEY_EXTRA_DATA: String = "extra_data"
    }
}
