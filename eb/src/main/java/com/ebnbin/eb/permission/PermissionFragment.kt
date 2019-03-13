package com.ebnbin.eb.permission

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.app.EBFragment
import com.ebnbin.eb.app.FragmentHelper
import com.ebnbin.eb.app.removeSelf
import com.ebnbin.eb.util.ebApp

/**
 * 权限请求 Fragment.
 */
class PermissionFragment : EBFragment() {
    private lateinit var callback: Callback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = attachCallbackNotNull(Callback::class.java)
    }

    /**
     * 权限结果回调.
     */
    interface Callback {
        fun onPermissionsResult(
            callingId: String,
            permissions: List<String>,
            granted: Boolean,
            extraData: Map<String, Any?>
        )
    }

    //*****************************************************************************************************************

    private lateinit var callingId: String
    private lateinit var permissions: List<String>
    private lateinit var extraData: Map<String, Any?>

    private var hasSystemAlertWindowPermission: Boolean = false
    private lateinit var runtimePermissions: List<String>

    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        callingId = arguments.getString(KEY_CALLING_ID) ?: throw RuntimeException()
        permissions = arguments.getStringArrayList(KEY_PERMISSIONS) ?: throw RuntimeException()
        @Suppress("UNCHECKED_CAST")
        extraData = arguments.getSerializable(KEY_EXTRA_DATA) as Map<String, Any?>

        val runtimePermissions = ArrayList(permissions)
        hasSystemAlertWindowPermission = runtimePermissions.remove(Manifest.permission.SYSTEM_ALERT_WINDOW)
        this.runtimePermissions = runtimePermissions
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
        checkSystemAlertWindowPermission(true)
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

    private fun requestSystemAlertWindowPermission() {
        startSettingsActivity(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION)
    }

    private fun requestRuntimePermissions() {
        requestPermissions(runtimePermissions.toTypedArray(), REQUEST_CODE_RUNTIME_PERMISSIONS)
    }

    private fun requestRuntimePermissionsDeniedForever() {
        startSettingsActivity(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            REQUEST_CODE_RUNTIME_PERMISSIONS_DENIED_FOREVER)
    }

    private fun startSettingsActivity(action: String, requestCode: Int) {
        try {
            val intent = Intent(action).setData(Uri.parse("package:${ebApp.packageName}"))
            startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            onPermissionsResult(false)
        }
    }

    //*****************************************************************************************************************

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
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
                    // 正常情况下不应该发生.
                } else {
                    checkRuntimePermissions(CheckRuntimePermissions.REQUEST_RESULT)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    //*****************************************************************************************************************

    private fun onPermissionsResult(granted: Boolean) {
        callback.onPermissionsResult(callingId, permissions, granted, extraData)
        removeSelf()
    }

    //*****************************************************************************************************************

    companion object {
        private const val KEY_CALLING_ID = "calling_id"
        private const val KEY_PERMISSIONS = "permissions"
        private const val KEY_EXTRA_DATA = "extra_data"

        private const val REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION = 0x1
        private const val REQUEST_CODE_RUNTIME_PERMISSIONS = 0x2
        private const val REQUEST_CODE_RUNTIME_PERMISSIONS_DENIED_FOREVER = 0x3

        fun start(
            fm: FragmentManager,
            callingId: String,
            permissions: List<String>,
            fillExtraData: (Map<String, Any?>.() -> Unit)? = null
        ) {
            val extraData = LinkedHashMap<String, Any?>()
            fillExtraData?.invoke(extraData)
            FragmentHelper.add(fm, PermissionFragment::class.java) {
                putString(KEY_CALLING_ID, callingId)
                putStringArrayList(KEY_PERMISSIONS, ArrayList(LinkedHashSet(permissions)))
                putSerializable(KEY_EXTRA_DATA, extraData)
            }
        }
    }
}
