package com.ebnbin.sample

import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb.dev.CreateDevPage
import com.ebnbin.sample.dev.SampleDevPageFragment

class App : EBApp() {
    override val createDevPages: List<CreateDevPage>
        get() = listOf(
            CreateDevPage("Sample", SampleDevPageFragment::class.java)
        ) + super.createDevPages
}
