package com.ebnbin.eb.async

import android.content.Context
import com.ebnbin.eb.dialog.Cancel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface Loading<T> {
    fun onStart(observable: Observable<T>, loadings: LinkedHashMap<Observable<*>, Loading<*>>, disposable: Disposable)

    fun onSuccess(observable: Observable<T>, loadings: LinkedHashMap<Observable<*>, Loading<*>>, t: T)

    fun onFailure(
        observable: Observable<T>,
        loadings: LinkedHashMap<Observable<*>, Loading<*>>,
        throwable: Throwable,
        onRetry: (() -> Unit)?
    )

    companion object {
        fun <T> dialog(context: Context): Loading<T> {
            return DialogLoading(context, Cancel.CANCELABLE)
        }

        fun <T> dialogNotCanceledOnTouchOutside(context: Context): Loading<T> {
            return DialogLoading(context, Cancel.NOT_CANCELED_ON_TOUCH_OUTSIDE)
        }

        fun <T> dialogNotCancelable(context: Context): Loading<T> {
            return DialogLoading(context, Cancel.NOT_CANCELABLE)
        }
    }
}
