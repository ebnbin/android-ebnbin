package com.ebnbin.sample.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ebnbin.sample.R
import com.ebnbin.sample.databinding.SampleFragmentBinding

class SampleFragment : Fragment() {
    private val viewModel: SampleViewModel by lazy {
        ViewModelProviders.of(this).get(SampleViewModel::class.java)
    }

    private lateinit var viewDataBinding: SampleFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.sample_fragment, container, false)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.vm = viewModel
        return viewDataBinding.root
    }
}
