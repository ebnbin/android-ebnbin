package com.ebnbin.eb.net.model.eb

import com.ebnbin.eb.net.model.EBModel
import com.ebnbin.eb.util.versionCode

class Update : EBModel() {
    var version: Int = 0
    var minVersion: Int = 0
    lateinit var url: String
    lateinit var message: String

    fun hasUpdate(): Boolean {
        return versionCode < version
    }

    fun hasForceUpdate(): Boolean {
        return versionCode < minVersion
    }
}
