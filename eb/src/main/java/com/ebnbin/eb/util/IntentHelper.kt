package com.ebnbin.eb.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

object IntentHelper {
    fun openInBrowser(context: Context, url: String) {
        try {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
        }
    }

    fun openAppStore(context: Context) {
        try {
            val uri = Uri.parse("market://details?id=${ebApp.packageName}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
        }
    }
}
