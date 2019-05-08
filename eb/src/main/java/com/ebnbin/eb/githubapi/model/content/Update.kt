package com.ebnbin.eb.githubapi.model.content

import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.EBModel

class Update : EBModel {
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
