package com.ebnbin.eb.async

import com.ebnbin.eb.net.model.EBResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * 异步任务帮助类.
 */
class AsyncHelper(private val delegate: Delegate) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun onDestroy() {
        // TODO: dispose.
        compositeDisposable.clear()
    }

    /**
     * @param stopLoadingOnNext 如果为 true 则在第一次 onNext 回调中停止 loading, 否则在 onComplete 回调中停止 loading.
     */
    fun <T> observable(
        observable: Observable<T>,
        loading: Loading = Loading.NONE,
        stopLoadingOnNext: Boolean = true,
        onNext: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onSubscribe: ((Disposable) -> Unit)? = null
    ): Disposable {
        return observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (stopLoadingOnNext) {
                        // TODO: 只在第一次调用 onNext 时停止 loading.
                        delegate.stopLoading(loading, false, null, null)
                    }
                    onNext?.invoke(it)
                },
                {
                    delegate.stopLoading(loading, true, it) {
                        observable(observable, loading, stopLoadingOnNext, onNext, onError, onComplete, onSubscribe)
                    }
                    onError?.invoke(it)
                },
                {
                    if (!stopLoadingOnNext) {
                        delegate.stopLoading(loading, false, null, null)
                    }
                    onComplete?.invoke()
                },
                {
                    compositeDisposable.add(it)
                    delegate.startLoading(loading, it)
                    onSubscribe?.invoke(it)
                }
            )
    }

    fun <Response : EBResponse> request(
        observable: Observable<Response>,
        loading: Loading = Loading.NONE,
        onSuccess: ((Response) -> Unit)? = null,
        onFailure: ((Throwable) -> Unit)? = null,
        onStart: ((Disposable) -> Unit)? = null
    ): Disposable {
        return observable(observable, loading, true, onSuccess, onFailure, null, onStart)
    }

    fun <T> task(
        task: () -> T,
        loading: Loading = Loading.NONE,
        onSuccess: ((T) -> Unit)? = null,
        onFailure: ((Throwable) -> Unit)? = null,
        onStart: ((Disposable) -> Unit)? = null
    ): Disposable {
        return observable(
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
            loading, true, onSuccess, onFailure, null, onStart
        )
    }

    interface Delegate {
        fun startLoading(loading: Loading, disposable: Disposable)

        fun stopLoading(loading: Loading, error: Boolean, throwable: Throwable?, onRetry: (() -> Unit)?)
    }
}
