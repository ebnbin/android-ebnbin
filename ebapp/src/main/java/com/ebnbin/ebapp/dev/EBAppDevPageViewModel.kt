package com.ebnbin.ebapp.dev

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebnbin.eb.base64Decode
import com.ebnbin.eb.coroutine.CoroutineLiveData
import com.ebnbin.eb.coroutine.Loading
import com.ebnbin.eb.log
import com.ebnbin.ebapp.api.GitHubApi
import kotlinx.coroutines.Job

internal class EBAppDevPageViewModel : ViewModel() {
    val ebappJson: CoroutineLiveData<String?> = (CoroutineLiveData<String?>(viewModelScope) {
        GitHubApi.instance
            .getContentsFile("android-ebnbin", "ebapp/api/ebapp.json", "develop")
            .content
            ?.base64Decode()
            ?.toString(Charsets.UTF_8)
            .toString()
    }).also {
        it.addLoading(object : Loading<String?> {
            override fun onStart(job: Job) {
                super.onStart(job)
                log("onStart $job")
            }

            override fun onSuccess(result: String?) {
                super.onSuccess(result)
                log("onSuccess $result")
            }

            override fun onFailure(throwable: Throwable) {
                super.onFailure(throwable)
                log("onFailure $throwable")
            }
        })
    }
}
