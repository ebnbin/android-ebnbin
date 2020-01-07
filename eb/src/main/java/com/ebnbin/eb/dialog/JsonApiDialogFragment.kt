package com.ebnbin.eb.dialog

import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.ebnbin.eb.R
import com.ebnbin.eb.fragment.requireCallback
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class JsonApiDialogFragment : AlertDialogFragment() {
    private val viewModel: JsonApiDialogViewModel by viewModels()

    override fun onCreateAlertDialogBuilder(builder: AlertDialog.Builder) {
        builder.setView(R.layout.eb_dialog_json_api_dialog_fragment)
        super.onCreateAlertDialogBuilder(builder)
    }

    override fun onAlertDialogShow(alertDialog: AlertDialog) {
        val job = viewModel.viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            onJsonFailure(throwable)
        }) {
            val result = requireCallback<Callback>().onGetJson(this)
            onJsonSuccess(result)
        }
        onJsonStart(job)
        super.onAlertDialogShow(alertDialog)
    }

    protected open fun onJsonStart(job: Job) {
        alertDialog?.findViewById<ProgressBar>(R.id.eb_progress_bar)?.isVisible = true
        alertDialog?.findViewById<TextView>(R.id.eb_message)?.isVisible = false
    }

    protected open fun onJsonSuccess(json: String) {
        alertDialog?.findViewById<ProgressBar>(R.id.eb_progress_bar)?.isVisible = false
        alertDialog?.findViewById<TextView>(R.id.eb_message)?.isVisible = true
        alertDialog?.findViewById<TextView>(R.id.eb_message)?.text = json
    }

    protected open fun onJsonFailure(throwable: Throwable) {
        alertDialog?.findViewById<ProgressBar>(R.id.eb_progress_bar)?.isVisible = false
        alertDialog?.findViewById<TextView>(R.id.eb_message)?.isVisible = true
        alertDialog?.findViewById<TextView>(R.id.eb_message)?.text = throwable.toString()
    }

    interface Callback : AlertDialogFragment.Callback {
        suspend fun onGetJson(coroutineScope: CoroutineScope): String
    }
}
