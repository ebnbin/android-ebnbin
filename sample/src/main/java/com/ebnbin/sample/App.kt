package com.ebnbin.sample

import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.app2.EBApp
import com.ebnbin.sample.dev.SampleDevPageFragment

class App : EBApp() {
    override val devPages: List<Pair<Class<out PreferenceFragmentCompat>, CharSequence>>
        get() = listOf(SampleDevPageFragment::class.java to "Sample") + super.devPages
}
