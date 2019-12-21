package com.ebnbin.eb2.dev

import com.ebnbin.eb.EBApp
import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb2.util.EBModel
import com.ebnbin.eb2.util.WindowHelper

open class EBReport : EBModel {
    var displayRealSize: String? = null
    var displaySize: String? = null
    var displayRealWidthDp: Float? = null
    var displayRealHeightDp: Float? = null

    open fun create(): EBReport {
        displayRealSize = WindowHelper.displayRealSize.run { "${width0}x$height0" }
        displaySize = WindowHelper.displaySize.run { "${width0}x$height0" }
        displayRealWidthDp = WindowHelper.displayRealSize.width0 / EBApp.instance.resources.displayMetrics.density
        displayRealHeightDp = WindowHelper.displayRealSize.height0 / EBApp.instance.resources.displayMetrics.density
        return this
    }

    override fun toString(): String {
        return Libraries.gson.toJson(this)
    }
}
