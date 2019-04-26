package com.ebnbin.eb.net.model.eb

import com.ebnbin.eb.net.model.EBResponse
import com.ebnbin.eb.util.BuildHelper

class Update : EBResponse() {
    var version: Int = 0
    var minVersion: Int = 0
    lateinit var url: String
    lateinit var message: String

    fun hasUpdate(): Boolean {
        return BuildHelper.versionCode < version
    }

    fun hasForceUpdate(): Boolean {
        return BuildHelper.versionCode < minVersion
    }
}
