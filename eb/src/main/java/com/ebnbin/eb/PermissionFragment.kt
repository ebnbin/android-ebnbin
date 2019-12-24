package com.ebnbin.eb

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class PermissionFragment : Fragment(), AlertDialogFragment.Callback {
    private lateinit var permissions: LinkedHashSet<String>

    //*****************************************************************************************************************

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissions = LinkedHashSet(requireContext().getNeededPermissions(*requireArgument(KEY_PERMISSIONS)))
        if (savedInstanceState == null) {
            checkPermissions()
        }
    }

    //*****************************************************************************************************************

    private fun checkPermissions() {
        checkSystemAlertWindowPermission(true)
    }

    private fun checkSystemAlertWindowPermission(firstTime: Boolean) {
        if (firstTime && !permissions.remove(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            checkWriteSettingsPermission(true)
            return
        }
        if (requireContext().hasSystemAlertWindowPermission()) {
            checkWriteSettingsPermission(true)
            return
        }
        if (firstTime) {
            requestSystemAlertWindowPermission()
        } else {
            onPermissionResult(Result.DENIED_OPEN_SETTINGS, Manifest.permission.SYSTEM_ALERT_WINDOW)
        }
    }

    private fun checkWriteSettingsPermission(firstTime: Boolean) {
        if (firstTime && !permissions.remove(Manifest.permission.WRITE_SETTINGS)) {
            checkRequestInstallPackagesPermission(true)
            return
        }
        if (requireContext().hasWriteSettingsPermission()) {
            checkRequestInstallPackagesPermission(true)
            return
        }
        if (firstTime) {
            requestWriteSettingsPermission()
        } else {
            onPermissionResult(Result.DENIED_OPEN_SETTINGS, Manifest.permission.WRITE_SETTINGS)
        }
    }

    private fun checkRequestInstallPackagesPermission(firstTime: Boolean) {
        if (firstTime && !permissions.remove(Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
            checkRuntimePermissions(CheckRuntimePermission.FIRST_TIME)
            return
        }
        if (requireContext().hasRequestInstallPackagesPermission()) {
            checkRuntimePermissions(CheckRuntimePermission.FIRST_TIME)
            return
        }
        if (firstTime) {
            @Suppress("ImplicitThis")
            requestRequestInstallPackagesPermission()
        } else {
            onPermissionResult(Result.DENIED_OPEN_SETTINGS, Manifest.permission.REQUEST_INSTALL_PACKAGES)
        }
    }

    private fun checkRuntimePermissions(checkRuntimePermission: CheckRuntimePermission) {
        var nextCheckRuntimePermission = checkRuntimePermission
        permissions.toList().forEach {
            if (requireContext().hasRuntimePermission(it)) {
                permissions.remove(it)
                nextCheckRuntimePermission = CheckRuntimePermission.FIRST_TIME
                return@forEach
            }
            when (nextCheckRuntimePermission) {
                CheckRuntimePermission.FIRST_TIME -> {
                    requestRuntimePermission(it)
                }
                CheckRuntimePermission.REQUEST_RESULT -> {
                    if (shouldShowRequestPermissionRationale(it)) {
                        onPermissionResult(Result.DENIED_REQUEST, it)
                    } else {
                        requestRuntimePermissionOpenSettings(it)
                    }
                }
                CheckRuntimePermission.OPEN_SETTINGS_RESULT -> {
                    onPermissionResult(Result.DENIED_OPEN_SETTINGS, it)
                }
            }
            return
        }
        onPermissionResult(Result.GRANTED, null)
    }

    /**
     * 在什么情况下检查运行时权限.
     */
    private enum class CheckRuntimePermission {
        /**
         * 第一次检查.
         */
        FIRST_TIME,
        /**
         * 请求权限返回结果后检查.
         */
        REQUEST_RESULT,
        /**
         * 权限被永久拒绝时打开系统设置手动请求权限返回结果后检查.
         */
        OPEN_SETTINGS_RESULT
    }

    //*****************************************************************************************************************

    private fun requestSystemAlertWindowPermission() {
        val permission = Manifest.permission.SYSTEM_ALERT_WINDOW
        val openSettings = true
        val message = requireCallback<Callback>(requireArgument(KEY_FRAGMENT_CALLBACK))
            .onRequestPermission(requireContext(), permission, openSettings, requireArgument(KEY_CALLBACK_BUNDLE))
        val action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
        val requestCode = REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION
        if (message == null) {
            openSettings(permission, action, requestCode)
        } else {
            openAlertDialog(permission, openSettings, message, action, requestCode)
        }
    }

    private fun requestWriteSettingsPermission() {
        val permission = Manifest.permission.WRITE_SETTINGS
        val openSettings = true
        val message = requireCallback<Callback>(requireArgument(KEY_FRAGMENT_CALLBACK))
            .onRequestPermission(requireContext(), permission, openSettings, requireArgument(KEY_CALLBACK_BUNDLE))
        val action = Settings.ACTION_MANAGE_WRITE_SETTINGS
        val requestCode = REQUEST_CODE_WRITE_SETTINGS_PERMISSION
        if (message == null) {
            openSettings(permission, action, requestCode)
        } else {
            openAlertDialog(permission, openSettings, message, action, requestCode)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestRequestInstallPackagesPermission() {
        val permission = Manifest.permission.REQUEST_INSTALL_PACKAGES
        val openSettings = true
        val message = requireCallback<Callback>(requireArgument(KEY_FRAGMENT_CALLBACK))
            .onRequestPermission(requireContext(), permission, openSettings, requireArgument(KEY_CALLBACK_BUNDLE))
        val action = Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES
        val requestCode = REQUEST_CODE_REQUEST_INSTALL_PACKAGES_PERMISSION
        if (message == null) {
            openSettings(permission, action, requestCode)
        } else {
            openAlertDialog(permission, openSettings, message, action, requestCode)
        }
    }

    private fun requestRuntimePermission(runtimePermission: String) {
        val openSettings = false
        val message = requireCallback<Callback>(requireArgument(KEY_FRAGMENT_CALLBACK)).onRequestPermission(
            requireContext(),
            runtimePermission,
            openSettings,
            requireArgument(KEY_CALLBACK_BUNDLE)
        )
        if (message == null) {
            request(runtimePermission)
        } else {
            openAlertDialog(runtimePermission, openSettings, message, null, null)
        }
    }

    private fun requestRuntimePermissionOpenSettings(runtimePermission: String) {
        val openSettings = true
        val message = requireCallback<Callback>(requireArgument(KEY_FRAGMENT_CALLBACK)).onRequestPermission(
            requireContext(),
            runtimePermission,
            openSettings,
            requireArgument(KEY_CALLBACK_BUNDLE)
        )
        val action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val requestCode = REQUEST_CODE_RUNTIME_PERMISSION_OPEN_SETTINGS
        if (message == null) {
            openSettings(runtimePermission, action, requestCode)
        } else {
            openAlertDialog(runtimePermission, openSettings, message, action, requestCode)
        }
    }

    //*****************************************************************************************************************

    private fun openAlertDialog(
        permission: String,
        openSettings: Boolean,
        message: CharSequence,
        action: String?,
        requestCode: Int?
    ) {
        childFragmentManager.openAlertDialogFragment(
            message = message,
            positiveText = if (openSettings) {
                getString(R.string.eb_permission_open_settings)
            } else {
                getString(R.string.eb_permission_request)
            },
            negativeText = getString(R.string.eb_permission_deny),
            dialogCancelable = DialogCancelable.NOT_CANCELABLE,
            callbackBundle = bundleOf(
                "permission" to permission,
                "open_settings" to openSettings,
                "action" to action,
                "request_code" to requestCode
            )
        )
    }

    override fun onAlertDialogPositive(alertDialog: AlertDialog, callbackBundle: Bundle): Boolean {
        if (callbackBundle.requireValue("open_settings")) {
            openSettings(
                callbackBundle.requireValue("permission"),
                callbackBundle.requireValue("action"),
                callbackBundle.requireValue("request_code")
            )
        } else {
            request(callbackBundle.requireValue("permission"))
        }
        return super.onAlertDialogPositive(alertDialog, callbackBundle)
    }

    override fun onAlertDialogNegative(alertDialog: AlertDialog, callbackBundle: Bundle): Boolean {
        onPermissionResult(Result.DENIED_OPEN_SETTINGS, callbackBundle.requireValue("permission"))
        return super.onAlertDialogNegative(alertDialog, callbackBundle)
    }

    //*****************************************************************************************************************

    private fun openSettings(permission: String, action: String, requestCode: Int) {
        runCatching {
            openActivity(Intent(action, Uri.parse("package:${requireContext().applicationId}")), requestCode)
        }.onFailure {
            onPermissionResult(Result.DENIED_OPEN_SETTINGS_FAILURE, permission)
        }
    }

    private fun request(runtimePermission: String) {
        requestPermissions(arrayOf(runtimePermission), REQUEST_CODE_RUNTIME_PERMISSION)
    }

    //*****************************************************************************************************************

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION -> {
                checkSystemAlertWindowPermission(false)
            }
            REQUEST_CODE_WRITE_SETTINGS_PERMISSION -> {
                checkWriteSettingsPermission(false)
            }
            REQUEST_CODE_REQUEST_INSTALL_PACKAGES_PERMISSION -> {
                checkRequestInstallPackagesPermission(false)
            }
            REQUEST_CODE_RUNTIME_PERMISSION_OPEN_SETTINGS -> {
                checkRuntimePermissions(CheckRuntimePermission.OPEN_SETTINGS_RESULT)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_RUNTIME_PERMISSION -> {
                checkRuntimePermissions(CheckRuntimePermission.REQUEST_RESULT)
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    //*****************************************************************************************************************

    private fun onPermissionResult(result: Result, deniedPermission: String?) {
        val message = requireCallback<Callback>(requireArgument(KEY_FRAGMENT_CALLBACK))
            .onPermissionResult(requireContext(), result, deniedPermission, requireArgument(KEY_CALLBACK_BUNDLE))
        if (message != null) {
            requireContext().toast(message)
        }
        removeSelf()
    }

    /**
     * 权限被拒绝的类型.
     */
    enum class Result {
        /**
         * 所有权限都被允许.
         */
        GRANTED,
        /**
         * 运行时权限被拒绝.
         */
        DENIED_REQUEST,
        /**
         * 特殊权限被拒绝或运行时权限被永久拒绝.
         */
        DENIED_OPEN_SETTINGS,
        /**
         * 特殊权限被拒绝或运行时权限被永久拒绝后打开系统设置失败.
         */
        DENIED_OPEN_SETTINGS_FAILURE
    }

    //*****************************************************************************************************************

    interface Callback {
        /**
         * 有默认实现, 不建议修改返回值.
         *
         * @param openSettings 是否需要通过打开系统设置请求权限.
         *
         * @return 用于展示提示对话框的 message, 如果为 null 则不展示对话框直接请求权限.
         */
        fun onRequestPermission(
            context: Context,
            permission: String,
            openSettings: Boolean,
            callbackBundle: Bundle
        ): CharSequence? {
            return if (openSettings) {
                context.getString(R.string.eb_permission_request_open_settings, context.getPermissionName(permission))
            } else null
        }

        /**
         * 有默认实现, 不建议修改返回值.
         *
         * @param deniedPermission 如果 [result] 不为 [Result.GRANTED] 则为当前被拒绝的权限, 否则为 null.
         *
         * @return 用于弹出 toast 的 message, 如果为 null 则不弹出 toast.
         */
        fun onPermissionResult(
            context: Context,
            result: Result,
            deniedPermission: String?,
            callbackBundle: Bundle
        ): CharSequence? {
            return when (result) {
                Result.GRANTED -> null
                Result.DENIED_REQUEST, Result.DENIED_OPEN_SETTINGS -> {
                    if (deniedPermission == null) return null
                    context.getString(
                        R.string.eb_permission_denied_request,
                        context.getPermissionName(deniedPermission)
                    )
                }
                Result.DENIED_OPEN_SETTINGS_FAILURE -> {
                    if (deniedPermission == null) return null
                    context.getString(
                        R.string.eb_permission_denied_open_settings_failure,
                        context.getPermissionName(deniedPermission)
                    )
                }
            }
        }
    }

    //*****************************************************************************************************************

    companion object {
        private const val REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION = 0x1
        private const val REQUEST_CODE_WRITE_SETTINGS_PERMISSION = 0x2
        private const val REQUEST_CODE_REQUEST_INSTALL_PACKAGES_PERMISSION = 0x3
        private const val REQUEST_CODE_RUNTIME_PERMISSION = 0x4
        private const val REQUEST_CODE_RUNTIME_PERMISSION_OPEN_SETTINGS = 0x5

        private const val KEY_PERMISSIONS: String = "permissions"
        private const val KEY_FRAGMENT_CALLBACK: String = "fragment_callback"
        private const val KEY_CALLBACK_BUNDLE: String = "callback_bundle"

        fun createArguments(
            permissions: Array<out String>,
            fragmentCallback: FragmentCallback = FragmentCallback.PREFER_PARENT_FRAGMENT,
            callbackBundle: Bundle = Bundle.EMPTY
        ): Bundle {
            return bundleOf(
                KEY_PERMISSIONS to permissions,
                KEY_FRAGMENT_CALLBACK to fragmentCallback,
                KEY_CALLBACK_BUNDLE to callbackBundle
            )
        }
    }
}
