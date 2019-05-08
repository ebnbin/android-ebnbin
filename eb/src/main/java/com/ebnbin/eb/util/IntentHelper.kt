package com.ebnbin.eb.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.ebnbin.eb.R
import com.ebnbin.eb.activity.EBActivity
import com.ebnbin.eb.dev.DevHelper

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
            DevHelper.report(e)
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
            DevHelper.report(e)
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
            DevHelper.report(e)
            false
        }
    }

    private fun createStartActivityIntent(context: Context, activityClass: Class<out Activity>): Intent {
        return Intent(context, activityClass)
    }

    //*****************************************************************************************************************

    fun startFragment(context: Context, fragmentClass: Class<out Fragment>): Boolean {
        val intent = createStartFragmentIntent(context, null, fragmentClass)
        return startActivity(context, intent)
    }

    fun startFragment(context: Context, intent: Intent): Boolean {
        val validIntent = createStartFragmentIntent(context, intent, null)
        return startActivity(context, validIntent)
    }

    fun startFragmentFromActivity(
        activity: Activity,
        fragmentClass: Class<out Fragment>,
        requestCode: Int = 0
    ): Boolean {
        val intent = createStartFragmentIntent(activity, null, fragmentClass)
        return startActivityFromActivity(activity, intent, requestCode)
    }

    fun startFragmentFromActivity(activity: Activity, intent: Intent, requestCode: Int = 0): Boolean {
        val validIntent = createStartFragmentIntent(activity, intent, null)
        return startActivityFromActivity(activity, validIntent, requestCode)
    }

    fun startFragmentFromFragment(
        fragment: Fragment,
        fragmentClass: Class<out Fragment>,
        requestCode: Int = 0
    ): Boolean {
        val intent = createStartFragmentIntent(fragment.requireContext(), null, fragmentClass)
        return startActivityFromFragment(fragment, intent, requestCode)
    }

    fun startFragmentFromFragment(fragment: Fragment, intent: Intent, requestCode: Int = 0): Boolean {
        val validIntent = createStartFragmentIntent(fragment.requireContext(), intent, null)
        return startActivityFromFragment(fragment, validIntent, requestCode)
    }

    private fun createStartFragmentIntent(
        context: Context,
        intent: Intent?,
        fragmentClass: Class<out Fragment>?
    ): Intent {
        val result = if (intent == null) Intent() else Intent(intent)
        result.setClass(context, EBActivity::class.java)
        if (fragmentClass != null) {
            result.putExtra(EBActivity.KEY_FRAGMENT_CLASS, fragmentClass)
        }
        return result
    }

    //*****************************************************************************************************************

    fun restartApp(): Boolean {
        val intent = ebApp.packageManager.getLaunchIntentForPackage(BuildHelper.applicationId) ?: return false
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        return startActivity(ebApp, intent)
    }

    fun finishApp(): Boolean {
        val intent = ebApp.packageManager.getLaunchIntentForPackage(BuildHelper.applicationId) ?: return false
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .putExtra(EBActivity.KEY_FINISH, true)
        return startActivity(ebApp, intent)
    }

    fun startBrowser(context: Context, url: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val result = startActivity(context, intent)
        if (!result) {
            AppHelper.toast(context, R.string.eb_start_browser_error)
        }
        return result
    }

    fun startMarket(context: Context): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${BuildHelper.applicationId}"))
        val result = startActivity(context, intent)
        if (!result) {
            AppHelper.toast(context, R.string.eb_start_market_error)
        }
        return result
    }

    fun startSettingsFromFragment(fragment: Fragment, action: String, requestCode: Int): Boolean {
        val intent = Intent(action, Uri.parse("package:${BuildHelper.applicationId}"))
        return startActivityFromFragment(fragment, intent, requestCode)
    }
}
