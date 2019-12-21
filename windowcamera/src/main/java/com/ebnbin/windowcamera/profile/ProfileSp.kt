package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.EBApp
import com.ebnbin.eb.sharedpreferences.getSharedPreferencesName
import com.ebnbin.eb2.sharedpreferences.Sp

open class ProfileSp<T>(
    val key: String,
    val builder: () -> Builder<T>
) : Sp<T>(
    { key },
    { builder().defaultValue },
    { EBApp.instance.getSharedPreferencesName(ProfileHelper.getSharedPreferencesNamePostfix()) },
    null
) {
    class Builder<T>(
        val defaultValue: T,
        val isVisible: Boolean? = null,
        val isEnabled: Boolean? = null,
        val isLockable: Boolean? = null,
        val isLockedDefaultValue: Boolean? = null
    )
}
