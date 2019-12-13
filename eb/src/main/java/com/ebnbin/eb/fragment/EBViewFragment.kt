package com.ebnbin.eb.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * 带视图的基础 Fragment.
 */
abstract class EBViewFragment<VDB : ViewDataBinding> : EBFragment() {
    protected lateinit var binding: VDB
        private set

    protected abstract val layoutId: Int

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}