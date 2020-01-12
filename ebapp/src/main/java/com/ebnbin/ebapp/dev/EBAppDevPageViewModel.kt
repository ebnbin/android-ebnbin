package com.ebnbin.ebapp.dev

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.base64Decode
import com.ebnbin.eb.coroutine.CoroutineLiveData
import com.ebnbin.eb.coroutine.Loading
import com.ebnbin.eb.log
import com.ebnbin.eb.net.DownloadListener
import com.ebnbin.eb.net.download
import com.ebnbin.ebapp.api.GitHubApi
import kotlinx.coroutines.Job
import okhttp3.MediaType
import okhttp3.ResponseBody
import java.io.File

internal class EBAppDevPageViewModel : ViewModel() {
    val ebappJson: CoroutineLiveData<String> = (CoroutineLiveData<String>(viewModelScope) {
        GitHubApi.instance
            .getContentsFile("android-ebnbin", "ebapp/api/ebapp.json", "develop")
            .content
            ?.base64Decode()
            ?.toString(Charsets.UTF_8)
            .toString()
    }).also {
        it.addLoading(object : Loading<String> {
            override fun onStart(job: Job) {
                super.onStart(job)
                log("onStart $job")
            }

            override fun onSuccess(result: String) {
                super.onSuccess(result)
                log("onSuccess $result")
            }

            override fun onFailure(throwable: Throwable) {
                super.onFailure(throwable)
                log("onFailure $throwable")
            }
        })
    }

    val releases: CoroutineLiveData<Any> = (CoroutineLiveData<Any>(viewModelScope) {
        GitHubApi.instance.getReleases("android-ebnbin")
    }).also {
        it.addLoading(object : Loading<Any> {
            override fun onStart(job: Job) {
                super.onStart(job)
                log("onStart $job")
            }

            override fun onSuccess(result: Any) {
                super.onSuccess(result)
                log("onSuccess $result")
            }

            override fun onFailure(throwable: Throwable) {
                super.onFailure(throwable)
                log("onFailure $throwable")
            }
        })
    }

    val releaseAsset: CoroutineLiveData<ResponseBody> = (CoroutineLiveData<ResponseBody>(viewModelScope) {
        val responseBody = GitHubApi.instance.getReleaseAsset("android-ebnbin", 13279496L)
        responseBody.download(object : DownloadListener {
            override fun onStart(contentType: MediaType?, contentLength: Long): File {
                return File(EBApplication.instance.getExternalFilesDir(null), "test.apk")
            }

            override fun onProgress(percent: Float, currentLength: Long, totalLength: Long) {
                super.onProgress(percent, currentLength, totalLength)
                log("$percent,$currentLength,$totalLength,${Thread.currentThread()}")
            }

            override fun onSuccess() {
                super.onSuccess()
                log("onSuccess")
            }

            override fun onFailure() {
                super.onFailure()
                log("onFailure")
            }
        })
        responseBody
    }).also {
        it.addLoading(object : Loading<ResponseBody> {
            override fun onStart(job: Job) {
                super.onStart(job)
                log("onStart $job")
            }

            override fun onSuccess(result: ResponseBody) {
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
