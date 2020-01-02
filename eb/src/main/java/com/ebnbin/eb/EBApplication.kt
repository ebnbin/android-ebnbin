package com.ebnbin.eb

import android.app.Application
import com.ebnbin.eb.dev.CreateDevPage
import com.ebnbin.eb.dev.EBDevPageFragment
import com.ebnbin.eb.dev.dev
import com.ebnbin.eb.dev.floating.DevFloatingActivityLifecycleCallbacks
import com.ebnbin.eb.report.Report

open class EBApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initDev()
    }

    private fun initDev() {
        if (!dev) return
        registerActivityLifecycleCallbacks(DevFloatingActivityLifecycleCallbacks)
    }

    open val createDevPages: List<CreateDevPage>
        get() = listOf(
            CreateDevPage("EB", EBDevPageFragment::class.java) {
                EBDevPageFragment.createArguments(it.toString())
            }
        )

    open val createReport: () -> Report
        get() = { Report() }

    companion object {
        lateinit var instance: EBApplication
            private set
    }
}
