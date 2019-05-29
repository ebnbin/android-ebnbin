package com.ebnbin.windowcamera.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.eb.util.ResHelper
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
                    invalidateActionMode()
                }
                MultiSelect.SELECTED -> {
                    adapter.data[position].multiSelect = MultiSelect.UNSELECTED
                    adapter.notifyItemChanged(position)
                    invalidateActionMode()
                }
            }

        }
        adapter.setOnItemLongClickListener { _, _, position ->
            when (adapter.data[position].multiSelect) {
                MultiSelect.NORMAL -> {
                    enterActionMode(position)
                }
                MultiSelect.UNSELECTED -> {
                    adapter.data[position].multiSelect = MultiSelect.SELECTED
                    adapter.notifyItemChanged(position)
                    invalidateActionMode()
                }
                MultiSelect.SELECTED -> {
                    // Do nothing.
                }
            }
            true
        }

        adapter.setNewData(imageVideos)
        recycler_view.adapter = adapter

        exitActionMode(true)

        view.doOnLayout {
            val spanCount = (it.width.pxToDp / 90f).toInt()
            val layoutManager = GridLayoutManager(requireContext(), spanCount)
            recycler_view.layoutManager = layoutManager
        }
    }

    override fun onBackPressed(): Boolean {
        if (isActionMode()) {
            exitActionMode()
            return true
        }
        return super.onBackPressed()
    }

    private fun isActionMode(): Boolean {
        return toolbar.tag == true
    }

    private fun enterActionMode(position: Int = -1) {
        if (isActionMode()) return
        toolbar.tag = true

        toolbar.setNavigationIcon(R.drawable.album_close)
        toolbar.setNavigationOnClickListener {
            exitActionMode()
        }
        toolbar.setTitleTextColor(ResHelper.getColorAttr(requireContext(), R.attr.colorPrimary))
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.album_action_mode)
        toolbar.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.delete -> {
                    exitActionMode()
                    true
                }
                else -> false
            }
        }

        adapter.data
            .filterIndexed { index, _ -> index != position }
            .forEach { it.multiSelect = MultiSelect.UNSELECTED }
        if (position != -1) {
            adapter.data[position].multiSelect = MultiSelect.SELECTED
        }
        adapter.notifyDataSetChanged()

        invalidateActionMode()
    }

    private fun exitActionMode(isInit: Boolean = false) {
        if (!isActionMode() && !isInit) return
        toolbar.tag = false

        toolbar.setNavigationIcon(R.drawable.eb_toolbar_back)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        toolbar.setTitle(R.string.album)
        toolbar.setTitleTextColor(ResHelper.getColorAttr(requireContext(), android.R.attr.textColorPrimary))
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.album_toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.multi_select -> {
                    enterActionMode()
                    true
                }
                else -> false
            }
        }

        if (!isInit) {
            adapter.data.forEach {
                it.multiSelect = MultiSelect.NORMAL
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun invalidateActionMode() {
        if (!isActionMode()) return
        val selectedCount = adapter.data.filter { it.multiSelect == MultiSelect.SELECTED }.size
        toolbar.title = getString(R.string.album_selected, selectedCount)
    }
}
