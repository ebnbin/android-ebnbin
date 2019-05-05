package com.ebnbin.eb.async

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AsyncHelper {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun clear() {
        compositeDisposable.clear()
    }

    fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun <T> observable(
        observable: Observable<T>,
        onNext: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onSubscribe: ((Disposable) -> Unit)? = null
    ): Disposable {
        return observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onNext?.invoke(it) },
                { onError?.invoke(it) },
                { onComplete?.invoke() },
                {
                    compositeDisposable.add(it)
                    onSubscribe?.invoke(it)
                }
            )
    }

    private val loadings: LinkedHashMap<Observable<*>, Loading<*>> = LinkedHashMap()

    fun <T> load(
        observable: Observable<T>,
        loading: Loading<T>? = null,
        onSuccess: ((T) -> Unit)? = null,
        onFailure: ((Throwable) -> Unit)? = null,
        onStart: ((Disposable) -> Unit)? = null
    ): Disposable {
        return observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    loading?.onSuccess(observable, loadings, it)
                    onSuccess?.invoke(it)
                },
                {
                    loading?.onFailure(observable, loadings, it) {
                        load(observable, loading, onSuccess, onFailure, onStart)
                    }
                    onFailure?.invoke(it)
                },
                {
                },
                {
                    loading?.onStart(observable, loadings, it)
                    onStart?.invoke(it)
                }
            )
    }

    fun <T> task(
        task: () -> T,
        loading: Loading<T>? = null,
        onSuccess: ((T) -> Unit)? = null,
        onFailure: ((Throwable) -> Unit)? = null,
        onStart: ((Disposable) -> Unit)? = null
    ): Disposable {
        return load(
            Observable.create<T> {
                try {
                    val result = task()
                    if (!it.isDisposed) {
                        it.onNext(result)
                        it.onComplete()
                    }
                } catch (throwable: Throwable) {
                    if (!it.isDisposed) {
                        it.onError(throwable)
                    }
                }
            },
            loading, onSuccess, onFailure, onStart
        )
    }
}
