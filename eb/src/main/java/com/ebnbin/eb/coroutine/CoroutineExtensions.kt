package com.ebnbin.eb.coroutine

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun <T> CoroutineScope.load(loading: Loading<T>, block: suspend CoroutineScope.() -> T) {
    val job = launch(CoroutineExceptionHandler { _, throwable ->
        loading.onFailure(throwable)
    }) {
        val result = block.invoke(this)
        loading.onSuccess(result)
    }
    loading.onStart(job)
}
