package com.ebnbin.windowcamera

import com.ebnbin.eb.dev.CreateDevPage
import com.ebnbin.ebapp.EBAppApplication
import com.ebnbin.windowcamera.dev.AppDevPageFragment

class AppApplication : EBAppApplication() {
    override val createDevPages: List<CreateDevPage>
        get() = listOf(
            CreateDevPage(getString(R.string.eb_label), AppDevPageFragment::class.java)
        ) + super.createDevPages
}
