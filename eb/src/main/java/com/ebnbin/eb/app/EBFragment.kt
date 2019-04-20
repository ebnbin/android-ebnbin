package com.ebnbin.eb.app

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.ebnbin.eb.async.AsyncHelper
import com.ebnbin.eb.async.Loading
import com.ebnbin.eb.async.LoadingDialogFragment
import com.ebnbin.eb.library.eventBus
import io.reactivex.disposables.Disposable

/**
 * Base Fragment.
 */
abstract class EBFragment : Fragment(), AsyncHelper.Delegate, LoadingDialogFragment.Callback {
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

    @Suppress("LeakingThis")
    protected val asyncHelper: AsyncHelper = AsyncHelper(this)

    override fun onDestroyView() {
        asyncHelper.onDestroy()
        super.onDestroyView()
    }

    override fun startLoading(loading: Loading, disposable: Disposable) {
        when (loading) {
            Loading.NONE -> Unit
            Loading.DIALOG -> {
                showLoadingDialog(false)
            }
        }
    }

    override fun stopLoading(loading: Loading, error: Boolean, throwable: Throwable?, onRetry: (() -> Unit)?) {
        when (loading) {
            Loading.NONE -> Unit
            Loading.DIALOG -> {
                hideLoadingDialog()
            }
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
