package com.ebnbin.windowcamera.viewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebnbin.eb.applicationId
import com.ebnbin.windowcamera.databinding.ViewerVideoPageFragmentBinding
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class ViewerVideoPageFragment : ViewerPageFragment() {
    private lateinit var binding: ViewerVideoPageFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ViewerVideoPageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onResume() {
        super.onResume()
        if (binding.playerView.player == null) {
            val dataSource = DefaultDataSourceFactory(
                requireContext(),
                Util.getUserAgent(requireContext(), requireContext().applicationId)
            )
            val mediaSource = ProgressiveMediaSource.Factory(dataSource).createMediaSource(viewerItem.uri)
            val player = SimpleExoPlayer.Builder(requireContext()).build()
            player.prepare(mediaSource)
            binding.playerView.player = player
        }
    }

    override fun onDestroyView() {
        binding.playerView.player?.release()
        super.onDestroyView()
    }
}
