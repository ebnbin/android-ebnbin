package com.ebnbin.windowcamera.viewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebnbin.eb.applicationId
import com.ebnbin.windowcamera.databinding.ViewerVideoPageFragmentBinding
import com.google.android.exoplayer2.Player
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

    private var reset: Boolean = true
    private var playWhenReady: Boolean = true
    private var currentPosition: Long = 0L

    override fun onResume() {
        super.onResume()
        if (reset) {
            playWhenReady = true
            currentPosition = 0L
        }
        val dataSource = DefaultDataSourceFactory(
            requireContext(),
            Util.getUserAgent(requireContext(), requireContext().applicationId)
        )
        val mediaSource = ProgressiveMediaSource.Factory(dataSource).createMediaSource(viewerItem.uri)
        val player = SimpleExoPlayer.Builder(requireContext()).build()
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.playWhenReady = playWhenReady
        player.seekTo(currentPosition)
        player.prepare(mediaSource, reset, reset)
        binding.playerView.player = player
        binding.playerView.controllerShowTimeoutMs = 3000
        binding.playerView.onResume()
        reset = false
    }

    override fun onPause() {
        binding.playerView.onPause()
        binding.playerView.player?.let {
            playWhenReady = it.playWhenReady
            currentPosition = it.currentPosition
            it.release()
            binding.playerView.player = null
        }
        super.onPause()
    }

    override fun onPageUnselected() {
        super.onPageUnselected()
        reset = true
    }
}
