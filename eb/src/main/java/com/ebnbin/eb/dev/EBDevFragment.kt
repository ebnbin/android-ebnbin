package com.ebnbin.eb.dev

import android.os.Bundle
import android.view.View
import com.ebnbin.eb.R
import com.ebnbin.eb.databinding.EbDevFragmentBinding
import com.ebnbin.eb.fragment.EBViewFragment

/**
 * 开发者选项页面.
 */
open class EBDevFragment : EBViewFragment<EbDevFragmentBinding>() {
    override val layoutId: Int = R.layout.eb_dev_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setCloseOnClick {
            activity?.finish()
        }
    }

    protected fun addDevItem(
        title: CharSequence,
        summary: CharSequence? = null,
        onClick: ((DevItemView) -> Unit)? = null
    ): DevItemView {
        return DevItemView(requireContext(), title, summary, onClick).also {
            binding.ebLinearLayout.addView(it)
        }
    }
}
