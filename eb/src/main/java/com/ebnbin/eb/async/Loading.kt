package com.ebnbin.eb.async

import android.content.Context
import com.ebnbin.eb.dialog.Cancel
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
