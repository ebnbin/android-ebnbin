package com.ebnbin.eb.dialog

import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import com.ebnbin.eb.R

class LoadingDialog(context: Context, cancel: Cancel, onDismiss: (LoadingDialog) -> Unit) : AppCompatDialog(context) {
    init {
        setCancelable(cancel != Cancel.NOT_CANCELABLE)
        setCanceledOnTouchOutside(cancel == Cancel.CANCELABLE)
        setContentView(R.layout.eb_loading_dialog_fragment)
        setOnDismissListener { onDismiss(this) }
    }
}
