package com.ebnbin.windowcamera.imagevideo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebnbin.eb.applicationId
import com.ebnbin.windowcamera.R
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.video_page_fragment.*

class VideoPageFragment : BaseImageVideoPageFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.video_page_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val player = SimpleExoPlayer.Builder(requireContext()).build()
        player_view.player = player
        val dataSourceFactory = DefaultDataSourceFactory(requireContext(), Util.getUserAgent(requireContext(), requireContext().applicationId))
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(imageVideo.uri)
        player.prepare(mediaSource)
    }

    override fun onDestroyView() {
        player_view.player?.release()
        super.onDestroyView()
    }
}
