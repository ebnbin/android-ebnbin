package com.ebnbin.eb.about

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ebnbin.eb.R
import com.ebnbin.eb.databinding.EbAboutFragmentBinding
import com.ebnbin.eb.fragment.EBViewFragment

internal class AboutFragment : EBViewFragment<EbAboutFragmentBinding>() {
    override val layoutId: Int = R.layout.eb_about_fragment

    private val viewModel: AboutFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.setBackOnClick {
            activity?.onBackPressed()
        }
        binding.setIconOnLongClick {
            true
        }
        binding.setVersionOnClick {
        }
    }
}
