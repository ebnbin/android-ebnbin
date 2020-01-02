package com.ebnbin.eb2.dev

import com.ebnbin.eb.library.gson
import com.ebnbin.eb2.util.EBModel

open class EBReport : EBModel {
    open fun create(): EBReport {
        return this
    }

    override fun toString(): String {
        return gson.toJson(this)
    }
}
