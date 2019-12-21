package com.ebnbin.eb.dev

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebnbin.eb.ebnbingithubapi.EbnbinGitHubApi
import com.ebnbin.eb.util.base64DecodeToString
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

internal class DevFragmentViewModel : ViewModel() {
    val sampleJson: MutableLiveData<String?> = MutableLiveData()

    val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        sampleJson.value = throwable.toString()
    }

    fun getSampleJson() {
        viewModelScope.launch(coroutineExceptionHandler) {
            sampleJson.value = EbnbinGitHubApi.instance
                .getContentsFile("api-android-ebnbin", "sample/sample.json")
                .content
                ?.toByteArray()
                ?.base64DecodeToString()
                .toString()
        }
    }
}
