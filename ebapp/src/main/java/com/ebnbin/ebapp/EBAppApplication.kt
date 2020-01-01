package com.ebnbin.ebapp

import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.dev.CreateDevPage
import com.ebnbin.ebapp.dev.EBAppDevPageFragment

open class EBAppApplication : EBApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override val createDevPages: List<CreateDevPage>
        get() = listOf(
            CreateDevPage("EB App", EBAppDevPageFragment::class.java)
        ) + super.createDevPages

    companion object {
        lateinit var instance: EBAppApplication
            private set
    }
}
