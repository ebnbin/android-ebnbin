package com.ebnbin.windowcamera.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.eb.util.ResHelper
import com.ebnbin.eb.util.WindowHelper
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

        val width = WindowHelper.getDisplaySize(requireContext()).width
        val spanCount = (width.pxToDp / 90f).toInt()
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        recycler_view.layoutManager = layoutManager

        adapter = AlbumAdapter()
        adapter.setOnItemClickListener { _, _, position ->
            when (adapter.data[position].multiSelect) {
                MultiSelect.NORMAL -> {
                    IntentHelper.startActivityFromFragment(this@AlbumFragment,
                        ImageVideoActivity.intent(requireContext(), imageVideos, position))
                }
                MultiSelect.UNSELECTED -> {
                    multiSelect(position, true)
                    invalidateActionMode()
                }
                MultiSelect.SELECTED -> {
                    multiSelect(position, false)
                    invalidateActionMode()
                }
            }

        }
        adapter.setOnItemLongClickListener { _, _, position ->
            when (adapter.data[position].multiSelect) {
                MultiSelect.NORMAL -> {
                    multiSelectEnter(position)
                    enterActionMode()
                }
                MultiSelect.UNSELECTED -> {
                    multiSelect(position, true)
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

        exitActionMode()
    }

    override fun onBackPressed(): Boolean {
        if (isActionMode()) {
            multiSelectNormal()
            exitActionMode()
            return true
        }
        return super.onBackPressed()
    }

    private fun isActionMode(): Boolean {
        return toolbar.tag == true
    }

    private fun enterActionMode() {
        toolbar.tag = true

        toolbar.setNavigationIcon(R.drawable.album_close)
        toolbar.setNavigationOnClickListener {
            multiSelectNormal()
            exitActionMode()
        }
        toolbar.setTitleTextColor(ResHelper.getColorAttr(requireContext(), R.attr.colorPrimary))
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.album_action_mode)
        toolbar.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.delete -> {
                    multiSelectNormal()
                    exitActionMode()
                    true
                }
                else -> false
            }
        }

        invalidateActionMode()
    }

    private fun invalidateActionMode() {
        if (!isActionMode()) return
        val selectedCount = adapter.data.filter { it.multiSelect == MultiSelect.SELECTED }.size
        toolbar.title = getString(R.string.album_selected, selectedCount)
    }

    private fun exitActionMode() {
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
                    multiSelectEnter()
                    enterActionMode()
                    true
                }
                else -> false
            }
        }
    }

    private fun multiSelectEnter(position: Int = -1) {
        adapter.data.forEachIndexed { index, albumItem ->
            albumItem.multiSelect = if (position == index) MultiSelect.SELECTED else MultiSelect.UNSELECTED
        }
        adapter.notifyDataSetChanged()
    }

    private fun multiSelect(position: Int, selected: Boolean) {
        adapter.data.getOrNull(position)?.let {
            it.multiSelect = if (selected) MultiSelect.SELECTED else MultiSelect.UNSELECTED
            adapter.notifyItemChanged(position)
        }
    }

    private fun multiSelectNormal() {
        adapter.data.forEach {
            it.multiSelect = MultiSelect.NORMAL
        }
        adapter.notifyDataSetChanged()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState ?: return
        val multiSelects = savedInstanceState.getSerializable("multi_selects") as ArrayList<MultiSelect>?
        if (multiSelects != null && multiSelects.size == adapter.data.size) {
            // 数量一致时才恢复状态.
            toolbar.tag = savedInstanceState.getBoolean("tag", false)
            multiSelects.forEachIndexed { index, multiSelect ->
                adapter.data[index].multiSelect = multiSelect
            }
            adapter.notifyDataSetChanged()
            if (isActionMode()) {
                enterActionMode()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("tag", toolbar.tag as? Boolean? ?: false)
        val multiSelects = ArrayList<MultiSelect>()
        adapter.data.forEach {
            multiSelects.add(it.multiSelect)
        }
        outState.putSerializable("multi_selects", multiSelects)
        super.onSaveInstanceState(outState)
    }
}
