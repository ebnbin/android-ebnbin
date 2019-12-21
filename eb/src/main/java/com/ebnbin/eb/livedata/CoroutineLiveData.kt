package com.ebnbin.eb.livedata

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent
import com.ebnbin.eb.loading.Loading
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class CoroutineLiveData<T>(
    private val coroutineScope: CoroutineScope,
    private val block: suspend CoroutineScope.(CoroutineLiveData<T>) -> T
) : LiveData<T>() {
    fun coroutineSetValue() {
        // TODO 去重.
        val job = coroutineScope.launch(CoroutineExceptionHandler { _, throwable ->
            loadings.forEach {
                it.onFailure(throwable)
            }
        }) {
            val result = block.invoke(this, this@CoroutineLiveData)
            value = result
            loadings.forEach {
                it.onSuccess(result)
            }
        }
        loadings.forEach {
            it.onStart(job)
        }
    }

    private val loadings: ArrayList<Loading<T>> = ArrayList()

    fun addLoading(loading: Loading<T>) {
        loadings.add(loading)
    }

    fun addLifecycleLoading(lifecycle: Lifecycle, loading: Loading<T>) {
        lifecycle.addObserver(object : LifecycleObserver {
            @Suppress("unused")
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                loadings.remove(loading)
            }
        })
        loadings.add(loading)
    }
}
