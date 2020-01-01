package com.ebnbin.eb.dev

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import kotlinx.android.parcel.Parcelize

@Parcelize
class DevPage(
    val title: CharSequence,
    val fragmentClass: Class<out Fragment>,
    val fragmentArguments: Bundle
) : Parcelable
