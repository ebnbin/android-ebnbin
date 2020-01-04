package com.ebnbin.windowcamera.viewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ebnbin.eb.fragment.requireArgument
import com.ebnbin.windowcamera.databinding.ViewerFragmentBinding

class ViewerFragment : Fragment() {
    private lateinit var binding: ViewerFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ViewerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.pagerAdapter = ViewerPagerAdapter(requireContext(), childFragmentManager,
            requireArgument(KEY_VIEWER_ITEMS))
        binding.setBackOnClick {
            activity?.finish()
        }
        if (savedInstanceState == null) {
            binding.executePendingBindings()
            binding.viewPager.currentItem = requireArgument(KEY_INDEX)
        }
    }

    companion object {
        private const val KEY_VIEWER_ITEMS: String = "viewer_items"
        private const val KEY_INDEX: String = "index"

        fun createArguments(viewerItems: List<ViewerItem>, index: Int = 0): Bundle {
            return bundleOf(
                KEY_VIEWER_ITEMS to ArrayList(viewerItems),
                KEY_INDEX to index
            )
        }
    }
}
