package com.ebnbin.windowcamera.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.eb.util.pxToDp
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.imagevideo.ImageVideo
import com.ebnbin.windowcamera.imagevideo.ImageVideoActivity
import com.ebnbin.windowcamera.util.IOHelper
import kotlinx.android.synthetic.main.album_fragment.*

class AlbumFragment : EBFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.album_fragment, container, false)
    }

    private lateinit var adapter: AlbumAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        IOHelper.refreshFiles()

        val imageVideos = ArrayList<ImageVideo>()
        IOHelper.files.forEach {
            val type = when (it.name.substringAfterLast(".", "")) {
                "jpg" -> ImageVideo.Type.IMAGE
                "mp4", "3gp" -> ImageVideo.Type.VIDEO
                else -> return@forEach
            }
            imageVideos.add(ImageVideo(type, it.absolutePath))
        }

        adapter = AlbumAdapter()
        recycler_view.adapter = adapter
        adapter.listener = object : AlbumAdapter.Listener {
            override fun onItemClick(position: Int) {
                IntentHelper.startActivityFromFragment(this@AlbumFragment,
                    ImageVideoActivity.intent(requireContext(), imageVideos, position))
            }
        }

        view.doOnLayout {
            val spanCount = (it.width.pxToDp / 90f).toInt()
            val layoutManager = GridLayoutManager(requireContext(), spanCount)
            recycler_view.layoutManager = layoutManager
        }
    }
}
