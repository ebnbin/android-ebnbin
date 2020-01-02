package com.ebnbin.eb.crash

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebnbin.eb.BuildConfig

internal class CrashViewModel : ViewModel() {
    val logVisible: MutableLiveData<Boolean> = MutableLiveData(BuildConfig.DEBUG)

    val log: MutableLiveData<CharSequence> = MutableLiveData()

    fun iconOnLongClick(view: View): Boolean {
        if (logVisible.value == true) return false
        logVisible.value = true
        return true
    }
}
