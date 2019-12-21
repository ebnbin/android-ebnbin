package com.ebnbin.eb2.dev

import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb2.util.EBModel

open class EBReport : EBModel {
    open fun create(): EBReport {
        return this
    }

    override fun toString(): String {
        return Libraries.gson.toJson(this)
    }
}
