package com.ebnbin.eb.app

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.ebnbin.eb.library.eventBus
import com.ebnbin.eb.loading.Loading
import com.ebnbin.eb.loading.LoadingDialogFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Base Fragment.
 */
abstract class EBFragment : Fragment(), LoadingDialogFragment.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isEventBusEnabled && !eventBus.isRegistered(this)) {
            eventBus.register(this)
        }

        onInitArguments(savedInstanceState, arguments ?: Bundle.EMPTY, activity?.intent?.extras ?: Bundle.EMPTY)
    }

    override fun onDestroy() {
        if (isEventBusEnabled && eventBus.isRegistered(this)) {
            eventBus.unregister(this)
        }
        super.onDestroy()
    }

    //*****************************************************************************************************************

    /**
     * 将父 Fragment 或 Activity 强转为 [callbackClass].
     */
    protected fun <T> attachCallback(callbackClass: Class<T>): T? {
        arrayOf(parentFragment, activity).forEach {
            if (callbackClass.isInstance(it)) {
                return callbackClass.cast(it)
            }
        }
        return null
    }

    protected fun <T> attachCallbackNotNull(callbackClass: Class<T>): T {
        return attachCallback(callbackClass) ?: throw RuntimeException()
    }

    //*****************************************************************************************************************

    /**
     * 是否注册 EventBus.
     */
    protected open val isEventBusEnabled: Boolean = false

    //*****************************************************************************************************************

    @CallSuper
    protected open fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
    }

    //*****************************************************************************************************************

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    private fun <T> async(
        observable: Observable<T>,
        loading: Loading = Loading.NONE,
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
                    // TODO 理论上应该在 onComplete 中调用 stopLoading.
                    stopLoading(loading)
                    onNext?.invoke(it)
                },
                {
                    stopLoading(loading)
                    onError?.invoke(it)
                },
                {
                    onComplete?.invoke()
                },
                {
                    compositeDisposable.add(it)
                    startLoading(loading)
                    onSubscribe?.invoke(it)
                })
    }

    protected fun <T> asyncRequest(
        observable: Observable<T>,
        loading: Loading = Loading.NONE,
        onNext: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onSubscribe: ((Disposable) -> Unit)? = null
    ): Disposable {
        return async(observable, loading, onNext, onError, onComplete, onSubscribe)
    }

    protected fun <T> asyncTask(
        task: () -> T,
        loading: Loading = Loading.NONE,
        onNext: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onSubscribe: ((Disposable) -> Unit)? = null
    ): Disposable {
        return async(
            Observable.create<T> {
                try {
                    it.onNext(task.invoke())
                    it.onComplete()
                } catch (throwable: Throwable) {
                    it.onError(throwable)
                }
            },
            loading, onNext, onError, onComplete, onSubscribe
        )
    }

    private fun startLoading(loading: Loading) {
        when (loading) {
            Loading.NONE -> Unit
            Loading.DIALOG -> showLoadingDialog(false)
        }
    }

    private fun stopLoading(loading: Loading) {
        when (loading) {
            Loading.NONE -> Unit
            Loading.DIALOG -> hideLoadingDialog()
        }
    }

    private var loadingDialogFragment: LoadingDialogFragment? = null

    protected fun showLoadingDialog(isCancelable: Boolean) {
        hideLoadingDialog()
        loadingDialogFragment = LoadingDialogFragment.start(childFragmentManager, isCancelable)
    }

    protected fun hideLoadingDialog() {
        loadingDialogFragment?.run {
            loadingDialogFragment = null
            dismiss()
        }
    }

    override fun onLoadingDialogDismiss() {
        loadingDialogFragment = null
    }
}
