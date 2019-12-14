package com.ebnbin.eb2.async

import io.reactivex.disposables.Disposable

interface Loading<T> {
    fun onStart(key: String, loadings: LinkedHashMap<String, Loading<*>>, disposable: Disposable)

    fun onSuccess(key: String, loadings: LinkedHashMap<String, Loading<*>>, t: T)

    fun onFailure(
        key: String,
        loadings: LinkedHashMap<String, Loading<*>>,
        throwable: Throwable,
        onRetry: (() -> Unit)?
    )
}
