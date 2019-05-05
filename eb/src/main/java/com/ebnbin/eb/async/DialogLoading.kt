package com.ebnbin.eb.async

import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import com.ebnbin.eb.R
import com.ebnbin.eb.dialog.Cancel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

internal class DialogLoading<T>(private val context: Context, private val cancel: Cancel) : Loading<T> {
    private var dialog: AppCompatDialog? = null

    override fun onStart(
        observable: Observable<T>,
        loadings: LinkedHashMap<Observable<*>, Loading<*>>,
        disposable: Disposable
    ) {
        val dialog = AppCompatDialog(context)
        dialog.setCancelable(cancel != Cancel.NOT_CANCELABLE)
        dialog.setCanceledOnTouchOutside(cancel == Cancel.CANCELABLE)
        dialog.setContentView(R.layout.eb_loading_dialog)
        dialog.setOnDismissListener {
            loadings.remove(observable)
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
        dialog.show()
        this.dialog = dialog
        loadings[observable] = this
    }

    override fun onSuccess(observable: Observable<T>, loadings: LinkedHashMap<Observable<*>, Loading<*>>, t: T) {
        stopLoading()
    }

    override fun onFailure(
        observable: Observable<T>,
        loadings: LinkedHashMap<Observable<*>, Loading<*>>,
        throwable: Throwable,
        onRetry: (() -> Unit)?
    ) {
        stopLoading()
    }

    private fun stopLoading() {
        dialog?.run {
            dialog = null
            dismiss()
        }
    }
}
