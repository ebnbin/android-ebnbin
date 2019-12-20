package com.ebnbin.eb.dev

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import com.ebnbin.eb.R
import com.ebnbin.eb.crash.CrashException
import com.ebnbin.eb.databinding.EbDevFragmentBinding
import com.ebnbin.eb.dialog.openAlertDialog
import com.ebnbin.eb.extension.toast
import com.ebnbin.eb.fragment.EBViewFragment

/**
 * 基础 Dev 页面.
 */
open class EBDevFragment : EBViewFragment<EbDevFragmentBinding>() {
    override val layoutId: Int
        get() = R.layout.eb_dev_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setCloseOnClick {
            activity?.finish()
        }
        onAddDevItems()
    }

    @CallSuper
    protected open fun onAddDevItems() {
        addDevItem("Calling Activity", arguments?.getString(KEY_CALLING_ACTIVITY))

        addDevItem("AlertDialogFragment", "isMaterial = false") {
            childFragmentManager.openAlertDialog(
                isMaterial = false,
                title = "Title",
                message = "Message",
                positiveText = "Positive",
                negativeText = "Negative"
            )
        }

        addDevItem("AlertDialogFragment", "isMaterial = true") {
            childFragmentManager.openAlertDialog(
                isMaterial = true,
                title = "Title",
                message = "Message",
                positiveText = "Positive",
                negativeText = "Negative"
            )
        }

        addDevItem("Toast") {
            Toast.makeText(requireContext(), "${System.currentTimeMillis()}", Toast.LENGTH_SHORT).show()
        }

        addDevItem("Simple Toast") {
            requireContext().toast("${System.currentTimeMillis()}")
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

    companion object {
        const val KEY_CALLING_ACTIVITY: String = "calling_activity"
    }
}
