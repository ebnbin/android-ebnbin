package com.ebnbin.eb2.githubapi.model.content

import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.versionCode
import com.ebnbin.eb2.util.EBModel

class Update : EBModel {
    var version: Int = 0
    var minVersion: Int = 0
    lateinit var url: String
    lateinit var md5: String
    lateinit var message: String

    fun hasUpdate(): Boolean {
        return EBApplication.instance.versionCode < version
    }

    fun hasForceUpdate(): Boolean {
        return EBApplication.instance.versionCode < minVersion
    }
}
