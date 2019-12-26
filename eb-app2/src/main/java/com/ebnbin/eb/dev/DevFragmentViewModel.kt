package com.ebnbin.eb.dev

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebnbin.eb.base64Decode
import com.ebnbin.eb.ebnbingithubapi.EbnbinGitHubApi
import com.ebnbin.eb.livedata.CoroutineLiveData
import com.ebnbin.eb.loading.Loading
import kotlinx.coroutines.Job

internal class DevFragmentViewModel : ViewModel() {
    val sampleJson: CoroutineLiveData<String?> = (CoroutineLiveData<String?>(viewModelScope) {
        EbnbinGitHubApi.instance
            .getContentsFile("api-android-ebnbin", "sample/sample.json")
            .content
            ?.base64Decode()
            ?.toString(Charsets.UTF_8)
            .toString()
    }).also {
        it.addLoading(object : Loading<String?> {
            override fun onStart(job: Job) {
                super.onStart(job)
                Log.e("ebnbin", "onStart")
            }

            override fun onSuccess(result: String?) {
                super.onSuccess(result)
                Log.e("ebnbin", "onSuccess")
            }

            override fun onFailure(throwable: Throwable) {
                super.onFailure(throwable)
                Log.e("ebnbin", "onFailure")
            }
        })
    }
}
