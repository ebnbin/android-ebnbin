package com.ebnbin.windowcamera.viewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebnbin.windowcamera.databinding.ViewerImagePageFragmentBinding

class ViewerImagePageFragment : ViewerPageFragment() {
    private lateinit var binding: ViewerImagePageFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ViewerImagePageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.uri = viewerItem.uri
    }
}
