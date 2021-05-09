package com.ebnbin.eb.dev

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.ebnbin.eb.databinding.EbDevFragmentBinding
import com.ebnbin.eb.fragment.requireArgument

internal class DevFragment : Fragment(), ViewPager.OnPageChangeListener {
    private lateinit var binding: EbDevFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = EbDevFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            activity?.finish()
        }
        binding.ebTabLayout.setupWithViewPager(binding.ebViewPager)
        binding.ebViewPager.adapter = DevPagerAdapter(requireContext(), childFragmentManager, requireArgument(KEY_DEV_PAGES))
        binding.ebViewPager.offscreenPageLimit = Int.MAX_VALUE
        binding.ebViewPager.currentItem = DevSpManager.dev_page.value
        binding.ebViewPager.addOnPageChangeListener(this)
    }

    override fun onDestroyView() {
        binding.ebViewPager.removeOnPageChangeListener(this)
        super.onDestroyView()
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

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        DevSpManager.dev_page.value = position
    }

    override fun onPageScrollStateChanged(state: Int) {
    }
}
