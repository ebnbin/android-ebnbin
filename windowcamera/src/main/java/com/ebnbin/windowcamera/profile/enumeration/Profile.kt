package com.ebnbin.windowcamera.profile.enumeration

import androidx.annotation.DrawableRes
import com.ebnbin.eb.util.res
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper

enum class Profile(val key: String, val title: CharSequence, @DrawableRes val iconId: Int) {
    DEFAULT("default", res.getString(R.string.profile_title_default), R.drawable.profile_default),
    WALKING("walking", res.getString(R.string.profile_title_walking), R.drawable.profile_walking),
    MIRROR("mirror", res.getString(R.string.profile_title_mirror), R.drawable.profile_mirror),
    CUSTOM_1("custom_1", res.getString(R.string.profile_title_custom_1), R.drawable.profile_custom),
    CUSTOM_2("custom_2", res.getString(R.string.profile_title_custom_2), R.drawable.profile_custom);

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
