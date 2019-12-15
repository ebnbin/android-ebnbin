package com.ebnbin.eb.dev

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import com.ebnbin.eb.R
import com.ebnbin.eb.crash.CrashException
import com.ebnbin.eb.databinding.EbDevFragmentBinding
import com.ebnbin.eb.dialog.AlertDialogFragment
import com.ebnbin.eb.extension.openAlertDialog
import com.ebnbin.eb.fragment.EBViewFragment

/**
 * 基础 Dev 页面.
 */
open class EBDevFragment : EBViewFragment<EbDevFragmentBinding>() {
    override val layoutId: Int = R.layout.eb_dev_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setCloseOnClick {
            activity?.finish()
        }
        onAddDevItems()
    }

    @CallSuper
    protected open fun onAddDevItems() {
        addDevItem("Calling Activity", activity?.callingActivity?.className.toString())

        addDevItem("AlertDialogFragment", "isMaterial = false") {
            parentFragmentManager.openAlertDialog(AlertDialogFragment.Builder(
                isMaterial = false,
                title = "Title",
                message = "Message",
                positiveButtonText = "Positive",
                negativeButtonText = "Negative"
            ))
        }

        addDevItem("AlertDialogFragment", "isMaterial = true") {
            parentFragmentManager.openAlertDialog(AlertDialogFragment.Builder(
                isMaterial = true,
                title = "Title",
                message = "Message",
                positiveButtonText = "Positive",
                negativeButtonText = "Negative"
            ))
        }

        addDevItem("Crash") {
            throw CrashException()
        }
    }

    protected fun addDevItem(
        title: CharSequence,
        summary: CharSequence? = null,
        onClick: ((DevItemView) -> Unit)? = null
    ): DevItemView {
        return DevItemView(requireContext(), title, summary, onClick).also {
            it.binding.lifecycleOwner = viewLifecycleOwner
            binding.ebLinearLayout.addView(it)
        }
    }
}
