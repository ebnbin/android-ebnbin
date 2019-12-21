package com.ebnbin.eb2.fragment

import android.os.Bundle
import androidx.annotation.CallSuper
import com.ebnbin.eb.widget.toast
import com.ebnbin.eb2.async.AsyncHelper
import com.ebnbin.eb2.util.TimeHelper
import com.ebnbin.windowcamera.R

/**
 * Base Fragment.
 *
 * 功能大部分同步给 EBDialogFragment 和 EBPreferenceFragment.
 */
abstract class EBFragment : com.ebnbin.eb.fragment.EBFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArguments(savedInstanceState)
    }

    override fun onDestroyView() {
        disposeAsyncHelper()
        super.onDestroyView()
    }

    //*****************************************************************************************************************

    private fun initArguments(savedInstanceState: Bundle?) {
        onInitArguments(savedInstanceState, arguments ?: Bundle(), activity?.intent?.extras ?: Bundle())
    }

    @CallSuper
    protected open fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
    }

    //*****************************************************************************************************************

    protected val asyncHelper: AsyncHelper = AsyncHelper()

    private fun disposeAsyncHelper() {
        asyncHelper.clear()
    }

    //*****************************************************************************************************************

    protected fun finish() {
        activity?.finish()
    }

    //*****************************************************************************************************************

    /**
     * @return 是否已经处理了 back 事件.
     */
    open fun onBackPressed(): Boolean {
        if (FragmentHelper.onBackPressed(childFragmentManager)) return true
        if (onBackFinish()) return true
        if (onDoubleBackFinish()) return true
        if (onMoveTaskToBack()) return true
        return false
    }

    /**
     * 是否启用返回键退出.
     */
    protected open val isBackFinishEnabled: Boolean = true

    private fun onBackFinish(): Boolean {
        return !isBackFinishEnabled
    }

    /**
     * 是否启用两次返回键退出.
     */
    protected open val isDoubleBackFinishEnabled: Boolean = false

    private var lastBackTimestamp: Long = 0L

    private fun onDoubleBackFinish(): Boolean {
        if (!isDoubleBackFinishEnabled) return false
        if (!TimeHelper.expired(lastBackTimestamp, DOUBLE_BACK_FINISH_EXPIRATION)) return false
        lastBackTimestamp = TimeHelper.long()
        requireContext().toast(R.string.eb_fragment_double_back_finish)
        return true
    }

    /**
     * 是否启用返回键后台.
     */
    protected open val isMoveTaskToBackEnabled: Boolean = false

    private fun onMoveTaskToBack(): Boolean {
        if (!isMoveTaskToBackEnabled) return false
        if (activity?.isTaskRoot != true) return false
        activity?.moveTaskToBack(false)
        return true
    }

    companion object {
        private const val DOUBLE_BACK_FINISH_EXPIRATION = 2000L
    }
}
