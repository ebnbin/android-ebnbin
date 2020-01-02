package com.ebnbin.ebapp.report

import androidx.annotation.Keep
import com.ebnbin.eb.report.Report
import com.ebnbin.ebapp.BuildConfig
import com.ebnbin.ebapp.EBAppApplication
import com.ebnbin.ebapp.deviceId
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
open class EBAppReport(
    val deviceId: String = EBAppApplication.instance.deviceId,
    val flavor: String = BuildConfig.FLAVOR
) : Report()
