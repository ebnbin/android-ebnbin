package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.sharedpreferences.Sp

open class ProfileSp<T>(
    key: String,
    val builder: (Profile) -> Builder<T>
) : Sp<T>(
    key,
    { builder(Profile.values().first { it.key == ProfileHelper.profile.value }).defaultValue },
    { ProfileHelper.getSharedPreferencesNamePostfix() }
) {
    class Builder<T>(val defaultValue: T, val isVisible: Boolean = true, val isEnabled: Boolean = true)
}
