package com.ebnbin.eb.dev

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import com.ebnbin.eb.R
import com.ebnbin.eb.databinding.EbDevFragmentBinding
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
