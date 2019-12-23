package com.ebnbin.eb.about

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.EBApp
import com.ebnbin.eb.time.toTimeString
import com.ebnbin.eb.versionName
import com.ebnbin.eb.viewmodel.EBViewModel

internal class AboutFragmentViewModel : EBViewModel() {
    val version: LiveData<CharSequence> = MutableLiveData(
        "v${EBApp.instance.versionName}　${BuildConfig.BUILD_TIMESTAMP.toTimeString("yyyy-MM-dd")}"
    )

    fun iconOnClick(view: View) {
    }
}
