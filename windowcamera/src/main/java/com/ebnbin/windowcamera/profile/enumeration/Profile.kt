package com.ebnbin.windowcamera.profile.enumeration

import com.ebnbin.eb.util.res
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper

enum class Profile(val key: String, val title: CharSequence) {
    DEFAULT("default", res.getString(R.string.profile_title_default)),
    WALKING("walking", res.getString(R.string.profile_title_walking)),
    SELFIE("selfie", res.getString(R.string.profile_title_selfie)),
    MIRROR("mirror", res.getString(R.string.profile_title_mirror)),
    CUSTOM_1("custom_1", res.getString(R.string.profile_title_custom_1)),
    CUSTOM_2("custom_2", res.getString(R.string.profile_title_custom_2));

    companion object {
        fun titles(): List<CharSequence> {
            return values().map { it.title }
        }

        fun get(key: String = ProfileHelper.profile.value): Profile {
            return values().first { it.key == key }
        }

        fun get(index: Int): Profile {
            return values()[index]
        }

        fun indexOf(key: String = ProfileHelper.profile.value): Int {
            return values().indexOf(get(key))
        }
    }
}
