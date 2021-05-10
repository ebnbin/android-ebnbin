package com.ebnbin.eb2.async

import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import com.ebnbin.eb.dialog.DialogCancelable
import com.ebnbin.windowcamera.R
import io.reactivex.disposables.Disposable

class DialogLoading<T>(private val context: Context, private val dialogCancelable: DialogCancelable) : Loading<T> {
    private var dialog: AppCompatDialog? = null

    override fun onStart(key: String, loadings: LinkedHashMap<String, Loading<*>>, disposable: Disposable) {
        val dialog = AppCompatDialog(context)
        dialog.setCancelable(dialogCancelable != DialogCancelable.NOT_CANCELABLE)
        dialog.setCanceledOnTouchOutside(dialogCancelable == DialogCancelable.CANCELABLE)
        dialog.setContentView(R.layout.eb_loading_dialog)
        dialog.setOnDismissListener {
            loadings.remove(key)
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
        dialog.show()
        this.dialog = dialog
        loadings[key] = this
    }

    override fun onSuccess(key: String, loadings: LinkedHashMap<String, Loading<*>>, t: T) {
        stopLoading()
    }

    override fun onFailure(
        key: String,
        loadings: LinkedHashMap<String, Loading<*>>,
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
