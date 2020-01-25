package com.ebnbin.eb.dialog

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.ebnbin.eb.R
import com.ebnbin.eb.copy
import com.ebnbin.eb.databinding.EbDialogJsonApiDialogFragmentBinding
import com.ebnbin.eb.fragment.FragmentCallback
import com.ebnbin.eb.fragment.getArgument
import com.ebnbin.eb.fragment.requireCallback
import com.ebnbin.eb.library.gson
import com.ebnbin.eb.toast
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class JsonApiDialogFragment : AlertDialogFragment() {
    private val viewModel: JsonApiDialogViewModel by viewModels()

    private lateinit var binding: EbDialogJsonApiDialogFragmentBinding

    override val title: CharSequence?
        get() = JsonApiDialogFragment::class.java.simpleName
    override val message: CharSequence?
        get() = null
    override val positiveText: CharSequence?
        get() = getString(R.string.eb_copy)
    override val negativeText: CharSequence?
        get() = getString(R.string.eb_cancel)
    override val neutralText: CharSequence?
        get() = getString(R.string.eb_dialog_json_api_request)

    override fun onCreateAlertDialogBuilder(builder: AlertDialog.Builder) {
        binding = EbDialogJsonApiDialogFragmentBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.isLoading.observe(this, Observer {
            val isLoading = it ?: true
            alertDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = !isLoading
            alertDialog?.getButton(AlertDialog.BUTTON_NEUTRAL)?.isEnabled = !isLoading
        })
        binding.request = getArgument(KEY_REQUEST)
        super.onCreateAlertDialogBuilder(builder)
    }

    override fun onAlertDialogShow(alertDialog: AlertDialog) {
        if (viewModel.response.value == null) {
            request()
        }
        super.onAlertDialogShow(alertDialog)
    }

    private fun request() {
        viewModel.isLoading.value = true
        viewModel.response.value = null
        viewModel.viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            viewModel.isLoading.value = false
            viewModel.response.value = throwable.toString()
        }) {
            viewModel.response.value =
                gson.toJson(requireCallback<Callback>().onGetJson(this, callbackBundle))
            viewModel.isLoading.value = false
        }
    }

    override fun onAlertDialogPositive(alertDialog: AlertDialog): Boolean {
        val response = viewModel.response.value
        if (response != null) {
            requireContext().copy(response)
            requireContext().toast(R.string.eb_copied)
        }
        return false
    }

    override fun onAlertDialogNeutral(alertDialog: AlertDialog): Boolean {
        if (viewModel.isLoading.value == false) {
            request()
        }
        return false
    }

    interface Callback : AlertDialogFragment.Callback {
        suspend fun onGetJson(coroutineScope: CoroutineScope, callbackBundle: Bundle): Any
    }

    companion object {
        private const val KEY_REQUEST: String = "request"

        fun createArguments(
            isMaterial: Boolean = false,
            request: CharSequence? = null,
            dialogCancelable: DialogCancelable = DialogCancelable.NOT_CANCELABLE_ON_TOUCH_OUTSIDE,
            fragmentCallback: FragmentCallback = FragmentCallback.PREFER_PARENT_FRAGMENT,
            callbackBundle: Bundle = Bundle.EMPTY
        ): Bundle {
            return AlertDialogFragment.createArguments(
                isMaterial = isMaterial,
                dialogCancelable = dialogCancelable,
                fragmentCallback = fragmentCallback,
                callbackBundle = callbackBundle
            ).also {
                it.putCharSequence(KEY_REQUEST, request)
            }
        }
    }
}
