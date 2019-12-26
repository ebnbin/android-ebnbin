package com.ebnbin.eb.app2.loading

import kotlinx.coroutines.Job

interface Loading<T> {
    fun onStart(job: Job) = Unit

    fun onSuccess(result: T) = Unit

    fun onFailure(throwable: Throwable) = Unit
}
