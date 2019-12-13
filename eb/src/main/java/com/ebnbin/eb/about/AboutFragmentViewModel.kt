package com.ebnbin.eb.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.ebApp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class AboutFragmentViewModel : ViewModel() {
    val version: LiveData<String> = MutableLiveData(
        "v${ebApp.packageManager.getPackageInfo(ebApp.packageName, 0).versionName}" +
                (if (BuildConfig.DEBUG) " ${BuildConfig.BUILD_TYPE}" else "") +
                "  ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(BuildConfig.BUILD_TIMESTAMP))}")
}
