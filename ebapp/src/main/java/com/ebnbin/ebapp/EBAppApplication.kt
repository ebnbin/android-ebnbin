package com.ebnbin.ebapp

import com.crashlytics.android.Crashlytics
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.dev.CreateDevPage
import com.ebnbin.eb.report.Report
import com.ebnbin.ebapp.dev.EBAppDevPageFragment
import com.ebnbin.ebapp.library.firebaseAnalytics
import com.ebnbin.ebapp.report.EBAppReport

open class EBAppApplication : EBApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initFirebaseAnalytics()
        initCrashlytics()
    }

    private fun initFirebaseAnalytics() {
        if (BuildConfig.DEBUG) return
        firebaseAnalytics.setUserId(deviceId)
    }

    private fun initCrashlytics() {
        if (com.ebnbin.eb.BuildConfig.DEBUG) return
        Crashlytics.setUserIdentifier(deviceId)
    }

    override val createDevPages: List<CreateDevPage>
        get() = listOf(
            CreateDevPage("EB App", EBAppDevPageFragment::class.java)
        ) + super.createDevPages

    override val createReport: () -> Report
        get() = { EBAppReport() }

    companion object {
        lateinit var instance: EBAppApplication
            private set
    }
}
