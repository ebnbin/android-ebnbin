package com.ebnbin.ebapp.dev

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.base64Decode
import com.ebnbin.eb.toast
import com.ebnbin.ebapp.api.GitHubApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

internal class EBAppDevPageViewModel : ViewModel() {
    val sample: MutableLiveData<String?> = MutableLiveData()

    fun coroutine() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            EBApplication.instance.toast(throwable.toString())
        }) {
            sample.value = GitHubApi.instance
                .getContentsFile("api-android-ebnbin", "sample/sample.json")
                .content
                ?.base64Decode()
                ?.toString(Charsets.UTF_8)
                .toString()
        }
    }
}
