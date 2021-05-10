package com.ebnbin.eb.coroutine

import kotlinx.coroutines.Job

interface Loading<T> {
    fun onStart(job: Job) = Unit

    fun onSuccess(result: T) = Unit

    fun onFailure(throwable: Throwable) = Unit
}
