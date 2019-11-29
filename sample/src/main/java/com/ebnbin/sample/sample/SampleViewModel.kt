package com.ebnbin.sample.sample

import android.app.Application
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ebnbin.eb.githubapi.GitHubApi
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.DataHelper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

class SampleViewModel(application: Application) : AndroidViewModel(application) {
    val time: MutableLiveData<String> = MutableLiveData()

    init {
        object : CountDownTimer(1000000L, 60L) {
            override fun onTick(millisUntilFinished: Long) {
                time.value = millisUntilFinished.toString()
            }

            override fun onFinish() {
                time.value = "onFinish"
            }
        }.start()
    }

    val json: MutableLiveData<String> = MutableLiveData("Sample")

    private val throwableCoroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        json.value = throwable.toString()
    }

    fun buttonAOnClick(view: View) {
        viewModelScope.launch(EmptyCoroutineContext + throwableCoroutineExceptionHandler) {
            val content = GitHubApi.api
                .coroutinesGetContentsFile("${BuildHelper.simpleApplicationId}/sample.json")
            json.value = DataHelper.base64DecodeToString(content.content)
        }
    }

    fun buttonBOnClick(view: View) {
        json.value = null
    }
}
