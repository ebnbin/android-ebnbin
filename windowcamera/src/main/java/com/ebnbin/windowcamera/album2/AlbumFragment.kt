package com.ebnbin.windowcamera.album2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.ebnbin.eb.activity.openActivity
import com.ebnbin.eb.dialog.AlertDialogFragment
import com.ebnbin.eb.dialog.openAlertDialogFragment
import com.ebnbin.eb.pxToDp
import com.ebnbin.eb.toast
import com.ebnbin.eb2.fragment.EBFragment
import com.ebnbin.eb2.util.ResHelper
import com.ebnbin.eb2.util.WindowHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.imagevideo.ImageVideo
import com.ebnbin.windowcamera.imagevideo.ImageVideoActivity
import com.ebnbin.windowcamera.util.IOHelper
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.android.synthetic.main.album_fragment.*

class AlbumFragment : EBFragment(), AlertDialogFragment.Callback {
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            multiSelectNormal()
            exitActionMode()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.album_fragment, container, false)
    }

    private lateinit var adapter: AlbumAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LiveEventBus.get("ScanFileEvent").observe(viewLifecycleOwner, Observer {
            invalidateAlbumItems()
        })

        val width = WindowHelper.getDisplaySize(requireContext()).width
        val spanCount = (requireContext().pxToDp(width) / 90f).toInt()
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        recycler_view.layoutManager = layoutManager

        adapter = AlbumAdapter()
        adapter.setOnItemClickListener { _, _, position ->
            when (adapter.data[position].multiSelect) {
                MultiSelect.NORMAL -> {
                    openActivity(ImageVideoActivity.intent(requireContext(), ArrayList(adapter.data), position))
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
        adapter.setEmptyView(R.layout.album_empty, recycler_view)
        recycler_view.adapter = adapter

        invalidateAlbumItems()
        exitActionMode()
    }

    private fun invalidateAlbumItems() {
        IOHelper.refreshFiles()
        val albumItems = ArrayList<AlbumItem>()
        IOHelper.files.forEachIndexed { index, file ->
            val type = when (file.name.substringAfterLast(".", "").toLowerCase()) {
                "jpg" -> ImageVideo.Type.IMAGE
                "mp4", "3gp" -> ImageVideo.Type.VIDEO
                else -> return@forEachIndexed
            }
            albumItems.add(AlbumItem(type, file, index))
        }
        adapter.setNewData(albumItems)
    }

    private fun isActionMode(): Boolean {
        return onBackPressedCallback.isEnabled
    }

    private fun enterActionMode() {
        onBackPressedCallback.isEnabled = true

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
                    if (adapter.data.none { albumItem -> albumItem.multiSelect == MultiSelect.SELECTED }) {
                        requireContext().toast(R.string.album_selected_empty)
                    } else {
                        childFragmentManager.openAlertDialogFragment(
                            message = getString(R.string.album_delete_message),
                            positiveText = getString(R.string.album_delete_position),
                            negativeText = getString(R.string.album_delete_negative),
                            fragmentTag = "delete"
                        )
                    }
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
        onBackPressedCallback.isEnabled = false

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
                R.id.refresh -> {
                    invalidateAlbumItems()
                    true
                }
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
            onBackPressedCallback.isEnabled = savedInstanceState.getBoolean("handle_on_back_pressed", false)
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
        outState.putBoolean("handle_on_back_pressed", onBackPressedCallback.isEnabled)
        val multiSelects = ArrayList<MultiSelect>()
        adapter.data.forEach {
            multiSelects.add(it.multiSelect)
        }
        outState.putSerializable("multi_selects", multiSelects)
        super.onSaveInstanceState(outState)
    }

    override fun onAlertDialogPositive(alertDialog: AlertDialog, callbackBundle: Bundle): Boolean {
        adapter.data
            .filter { it.multiSelect == MultiSelect.SELECTED }
            .forEach { it.file.delete() }
        invalidateAlbumItems()

        multiSelectNormal()
        exitActionMode()
        return true
    }
}
