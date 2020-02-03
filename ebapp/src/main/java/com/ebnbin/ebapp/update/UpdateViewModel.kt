package com.ebnbin.ebapp.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.base64Decode
import com.ebnbin.eb.coroutine.Loading
import com.ebnbin.eb.coroutine.load
import com.ebnbin.eb.library.gson
import com.ebnbin.ebapp.api.GitHubApi
import com.ebnbin.ebapp.ebnbinApplicationId

internal class UpdateViewModel : ViewModel() {
    fun requestUpdate(loading: Loading<GitHubApi.Update>) {
        viewModelScope.load(loading) {
            val content = GitHubApi.instance.getContentsFile(
                path = "${EBApplication.instance.ebnbinApplicationId}/api/update.json"
            ).content
            if (content == null) {
                // TODO
                throw RuntimeException()
            }
            gson.fromJson<GitHubApi.Update>(String(content.base64Decode()), GitHubApi.Update::class.java)
        }
    }
}
