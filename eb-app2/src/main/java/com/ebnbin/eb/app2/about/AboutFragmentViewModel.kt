package com.ebnbin.eb.app2.about

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.toTimeString
import com.ebnbin.eb.versionName

internal class AboutFragmentViewModel : ViewModel() {
    val version: LiveData<CharSequence> = MutableLiveData(
        "v${EBApplication.instance.versionName}　${BuildConfig.BUILD_TIMESTAMP.toTimeString("yyyy-MM-dd")}"
    )

    fun iconOnClick(view: View) {
    }
}
