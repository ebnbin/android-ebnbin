package com.ebnbin.eb.async

import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.dialog.Cancel
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
                        stopLoading(observable, loading, false, null, null)
                    }
                    onNext?.invoke(it)
                },
                {
                    stopLoading(observable, loading, true, it) {
                        observable(observable, loading, stopLoadingOnNext, onNext, onError, onComplete, onSubscribe)
                    }
                    onError?.invoke(it)
                },
                {
                    if (!stopLoadingOnNext) {
                        stopLoading(observable, loading, false, null, null)
                    }
                    onComplete?.invoke()
                },
                {
                    compositeDisposable.add(it)
                    startLoading(observable, loading, it)
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

    private fun startLoading(observable: Observable<*>, loading: Loading, disposable: Disposable) {
        when (loading) {
            Loading.NONE -> Unit
            Loading.DIALOG_CANCELABLE, Loading.DIALOG_NOT_CANCELED_ON_TOUCH_OUTSIDE, Loading.DIALOG_NOT_CANCELABLE -> {
                val fm = delegate.provideFragmentManager() ?: return
                showLoadingDialog(observable, fm, loading, disposable)
            }
        }
    }

    private fun stopLoading(
        observable: Observable<*>,
        loading: Loading,
        error: Boolean,
        throwable: Throwable?,
        onRetry: (() -> Unit)?
    ) {
        when (loading) {
            Loading.NONE -> Unit
            Loading.DIALOG_CANCELABLE, Loading.DIALOG_NOT_CANCELED_ON_TOUCH_OUTSIDE, Loading.DIALOG_NOT_CANCELABLE -> {
                hideLoadingDialog(observable)
            }
        }
    }

    //*****************************************************************************************************************

    private val loadingDialogFragments = HashMap<Observable<*>, LoadingDialogFragment>()

    private fun showLoadingDialog(
        observable: Observable<*>,
        fm: FragmentManager,
        loading: Loading,
        disposable: Disposable
    ) {
        val cancel = when (loading) {
            Loading.DIALOG_CANCELABLE -> Cancel.CANCELABLE
            Loading.DIALOG_NOT_CANCELED_ON_TOUCH_OUTSIDE -> Cancel.NOT_CANCELED_ON_TOUCH_OUTSIDE
            Loading.DIALOG_NOT_CANCELABLE -> Cancel.NOT_CANCELABLE
            else -> throw RuntimeException()
        }
        val loadingDialogFragment = LoadingDialogFragment.start(fm, cancel, extraData = hashMapOf(
            "observable" to observable,
            "disposable" to disposable
        ))
        loadingDialogFragments[observable] = loadingDialogFragment
    }

    private fun hideLoadingDialog(observable: Observable<*>) {
        loadingDialogFragments[observable]?.dismiss()
    }

    fun onLoadingDialogDismiss(cancel: Cancel, extraData: HashMap<*, *>) {
        val observable = extraData["observable"] as Observable<*>?
        val disposable = extraData["disposable"] as Disposable?

        if (observable != null) {
            loadingDialogFragments.remove(observable)
        }
        if (disposable?.isDisposed == false) {
            disposable.dispose()
        }
    }

    interface Delegate {
        /**
         * 用于添加 LoadingDialogFragment.
         */
        fun provideFragmentManager(): FragmentManager?
    }
}
