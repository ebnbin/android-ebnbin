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

        val imageVideos = ArrayList<AlbumItem>()
        IOHelper.files.forEachIndexed { index, file ->
            val type = when (file.name.substringAfterLast(".", "")) {
                "jpg" -> ImageVideo.Type.IMAGE
                "mp4", "3gp" -> ImageVideo.Type.VIDEO
                else -> return@forEachIndexed
            }
            imageVideos.add(AlbumItem(type, file, index))
        }

        adapter = AlbumAdapter()
        adapter.setOnItemClickListener { _, _, position ->
            when (adapter.data[position].multiSelect) {
                MultiSelect.NORMAL -> {
                    IntentHelper.startActivityFromFragment(this@AlbumFragment,
                        ImageVideoActivity.intent(requireContext(), imageVideos, position))
                }
                MultiSelect.UNSELECTED -> {
                    adapter.data[position].multiSelect = MultiSelect.SELECTED
                    adapter.notifyItemChanged(position)
                }
                MultiSelect.SELECTED -> {
                    adapter.data[position].multiSelect = MultiSelect.UNSELECTED
                    adapter.notifyItemChanged(position)
                }
            }

        }
        adapter.setOnItemLongClickListener { _, _, position ->
            when (adapter.data[position].multiSelect) {
                MultiSelect.NORMAL -> {
                    adapter.data
                        .filterIndexed { index, _ -> index != position }
                        .forEach { it.multiSelect = MultiSelect.UNSELECTED }
                    adapter.data[position].multiSelect = MultiSelect.SELECTED
                    adapter.notifyDataSetChanged()
                }
                MultiSelect.UNSELECTED -> {
                    adapter.data[position].multiSelect = MultiSelect.SELECTED
                    adapter.notifyItemChanged(position)
                }
                MultiSelect.SELECTED -> {
                    // Do nothing.
                }
            }
            true
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
