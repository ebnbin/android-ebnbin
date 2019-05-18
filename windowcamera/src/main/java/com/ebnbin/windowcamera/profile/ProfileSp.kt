package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.sharedpreferences.Sp

open class ProfileSp<T>(
    key: String,
    val builder: () -> Builder<T>
) : Sp<T>(
    key,
    { builder().defaultValue },
    { ProfileHelper.getSharedPreferencesNamePostfix() }
) {
    class Builder<T>(
        val defaultValue: T,
        val isVisible: Boolean = true,
        val isEnabled: Boolean = true,
        val isLockable: Boolean = true,
        val isLockedDefaultValue: Boolean = false
    )
}
