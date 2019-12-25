package com.ebnbin.eb2.fragment

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.ebnbin.eb.mainHandler
import com.ebnbin.eb.toast
import com.ebnbin.eb2.async.AsyncHelper
import com.ebnbin.windowcamera.R

/**
 * Base Fragment.
 *
 * 功能大部分同步给 EBDialogFragment 和 EBPreferenceFragment.
 */
abstract class EBFragment : Fragment() {
    protected val doubleBackFinishOnBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                isEnabled = false
                mainHandler.postDelayed({ isEnabled = true }, DOUBLE_BACK_FINISH_EXPIRATION)
                requireContext().toast(R.string.eb_fragment_double_back_finish)
            }
        }

    protected val moveTaskToBackOnBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                activity?.moveTaskToBack(true)
            }
        }

    protected val disableBackFinishOnBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, moveTaskToBackOnBackPressedCallback)
        requireActivity().onBackPressedDispatcher.addCallback(this, doubleBackFinishOnBackPressedCallback)
        requireActivity().onBackPressedDispatcher.addCallback(this, disableBackFinishOnBackPressedCallback)
    }

    //*****************************************************************************************************************

    override fun onDestroyView() {
        disposeAsyncHelper()
        super.onDestroyView()
    }

    //*****************************************************************************************************************

    protected val asyncHelper: AsyncHelper = AsyncHelper()

    private fun disposeAsyncHelper() {
        asyncHelper.clear()
    }

    //*****************************************************************************************************************

    companion object {
        private const val DOUBLE_BACK_FINISH_EXPIRATION = 5000L
    }
}
