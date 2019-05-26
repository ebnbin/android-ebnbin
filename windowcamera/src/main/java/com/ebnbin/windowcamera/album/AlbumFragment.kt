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
        IOHelper.files.forEachIndexed { index, file ->
            val type = when (file.name.substringAfterLast(".", "")) {
                "jpg" -> ImageVideo.Type.IMAGE
                "mp4", "3gp" -> ImageVideo.Type.VIDEO
                else -> return@forEachIndexed
            }
            imageVideos.add(ImageVideo(type, file.absolutePath, index))
        }

        adapter = AlbumAdapter()
        adapter.setOnItemClickListener { _, _, position ->
            IntentHelper.startActivityFromFragment(this@AlbumFragment,
                ImageVideoActivity.intent(requireContext(), imageVideos, position))
        }

        adapter.setNewData(imageVideos)
        recycler_view.adapter = adapter

        view.doOnLayout {
            val spanCount = (it.width.pxToDp / 90f).toInt()
            val layoutManager = GridLayoutManager(requireContext(), spanCount)
            recycler_view.layoutManager = layoutManager
        }
    }
}
