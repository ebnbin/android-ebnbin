package com.ebnbin.eb.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ebnbin.eb.R
import com.ebnbin.eb.dev.DevHelper

object IntentHelper {
    fun openBrowser(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            AppHelper.toast(context, R.string.eb_open_browser_error)
            DevHelper.report(e)
        }
    }

    fun openMarket(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${BuildHelper.applicationId}"))
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            AppHelper.toast(context, R.string.eb_open_market_error)
            DevHelper.report(e)
        }
    }
}
