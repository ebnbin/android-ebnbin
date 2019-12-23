package com.ebnbin.eb2.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.ebnbin.eb.EBApp
import com.ebnbin.eb.applicationId
import com.ebnbin.eb.toast
import com.ebnbin.eb2.activity.EBActivity
import com.ebnbin.eb2.dev.DevHelper
import com.ebnbin.windowcamera.R

object IntentHelper {
    fun startActivity(context: Context, activityClass: Class<out Activity>): Boolean {
        val intent = createStartActivityIntent(context, activityClass)
        return startActivity(context, intent)
    }

    fun startActivity(context: Context, intent: Intent): Boolean {
        return try {
            context.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            DevHelper.reportThrowable(e)
            false
        }
    }

    fun startActivityFromActivity(
        activity: Activity,
        activityClass: Class<out Activity>,
        requestCode: Int = 0
    ): Boolean {
        val intent = createStartActivityIntent(activity, activityClass)
        return startActivityFromActivity(activity, intent, requestCode)
    }

    fun startActivityFromActivity(activity: Activity, intent: Intent, requestCode: Int = 0): Boolean {
        return try {
            activity.startActivityForResult(intent, requestCode)
            true
        } catch (e: ActivityNotFoundException) {
            DevHelper.reportThrowable(e)
            false
        }
    }

    fun startActivityFromFragment(
        fragment: Fragment,
        activityClass: Class<out Activity>,
        requestCode: Int = 0
    ): Boolean {
        val intent = createStartActivityIntent(fragment.requireContext(), activityClass)
        return startActivityFromFragment(fragment, intent, requestCode)
    }

    fun startActivityFromFragment(fragment: Fragment, intent: Intent, requestCode: Int = 0): Boolean {
        return try {
            fragment.startActivityForResult(intent, requestCode)
            true
        } catch (e: ActivityNotFoundException) {
            DevHelper.reportThrowable(e)
            false
        }
    }

    private fun createStartActivityIntent(context: Context, activityClass: Class<out Activity>): Intent {
        return Intent(context, activityClass)
    }

    //*****************************************************************************************************************

    fun restartApp(): Boolean {
        val intent = EBApp.instance.packageManager.getLaunchIntentForPackage(EBApp.instance.applicationId) ?: return false
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        return startActivity(EBApp.instance, intent)
    }

    fun finishApp(): Boolean {
        val intent = EBApp.instance.packageManager.getLaunchIntentForPackage(EBApp.instance.applicationId) ?: return false
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .putExtra(EBActivity.KEY_FINISH, true)
        return startActivity(EBApp.instance, intent)
    }

    fun startBrowser(context: Context, url: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val result = startActivity(context, intent)
        if (!result) {
            context.toast(R.string.eb_start_browser_error)
        }
        return result
    }

    fun startMarket(context: Context): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${EBApp.instance.applicationId}"))
        val result = startActivity(context, intent)
        if (!result) {
            context.toast(R.string.eb_start_market_error)
        }
        return result
    }

    fun startSettingsFromFragment(fragment: Fragment, action: String, requestCode: Int): Boolean {
        val intent = Intent(action, Uri.parse("package:${EBApp.instance.applicationId}"))
        return startActivityFromFragment(fragment, intent, requestCode)
    }
}
