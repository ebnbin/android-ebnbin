package com.ebnbin.sample

import com.ebnbin.eb.dev.CreateDevPage
import com.ebnbin.ebapp.EBAppApplication
import com.ebnbin.sample.dev.SampleDevPageFragment

class App : EBAppApplication() {
    override val createDevPages: List<CreateDevPage>
        get() = listOf(
            CreateDevPage("Sample", SampleDevPageFragment::class.java)
        ) + super.createDevPages
}
