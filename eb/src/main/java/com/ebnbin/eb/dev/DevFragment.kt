package com.ebnbin.eb.dev

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ebnbin.eb.databinding.EbDevFragmentBinding
import com.ebnbin.eb.databinding.ViewPagerOnPageSelected
import com.ebnbin.eb.fragment.requireArgument

internal class DevFragment : Fragment() {
    private lateinit var binding: EbDevFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = EbDevFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.setCloseOnClick {
            activity?.finish()
        }
        binding.pagerAdapter = DevPagerAdapter(requireContext(), childFragmentManager, requireArgument(KEY_DEV_PAGES))
        binding.page = DevSpManager.page.value
        binding.onPageSelected = object : ViewPagerOnPageSelected {
            override fun onPageSelected(position: Int) {
                DevSpManager.page.value = position
            }
        }
    }

    companion object {
        private const val KEY_DEV_PAGES: String = "dev_pages"

        fun createArguments(
            devPages: List<DevPage>
        ): Bundle {
            return bundleOf(
                KEY_DEV_PAGES to ArrayList(devPages)
            )
        }
    }
}
