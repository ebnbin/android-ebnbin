package com.ebnbin.eb.app2.about

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb.toTimeString
import com.ebnbin.eb.versionName

internal class AboutFragmentViewModel : ViewModel() {
    val version: LiveData<CharSequence> = MutableLiveData(
        "v${EBApp.instance.versionName}　${BuildConfig.BUILD_TIMESTAMP.toTimeString("yyyy-MM-dd")}"
    )

    fun iconOnClick(view: View) {
    }
}
