package com.ebnbin.eb.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class EBDataBindingFragment<VDB : ViewDataBinding> : EBFragment() {
    protected lateinit var viewDataBinding: VDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO
        super.onCreateView(inflater, container, savedInstanceState)

        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewDataBinding.lifecycleOwner = this
        return viewDataBinding.root
    }

    protected abstract val layoutId: Int

    override fun onDestroyView() {
        viewDataBinding.unbind()
        super.onDestroyView()
    }
}
