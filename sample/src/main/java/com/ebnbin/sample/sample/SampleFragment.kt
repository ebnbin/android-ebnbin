package com.ebnbin.sample.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.ebnbin.eb.fragment.EBDataBindingFragment
import com.ebnbin.sample.R
import com.ebnbin.sample.databinding.SampleFragmentBinding

class SampleFragment : EBDataBindingFragment<SampleFragmentBinding>() {
    private val viewModel: SampleViewModel by lazy {
        ViewModelProviders.of(this).get(SampleViewModel::class.java)
    }

    override val layoutId: Int = R.layout.sample_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val result = super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding.vm = viewModel
        return result
    }
}
