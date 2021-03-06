package com.ebnbin.windowcamera.album2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.ebnbin.eb.dialog.AlertDialogFragment
import com.ebnbin.eb.dialog.openAlertDialogFragment
import com.ebnbin.eb.fragment.openFragment
import com.ebnbin.eb.pxToDp
import com.ebnbin.eb.toast
import com.ebnbin.eb2.fragment.EBFragment
import com.ebnbin.eb2.util.ResHelper
import com.ebnbin.eb2.util.WindowHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.databinding.Album2FragmentBinding
import com.ebnbin.windowcamera.util.IOHelper
import com.ebnbin.windowcamera.viewer.ViewerActivity
import com.ebnbin.windowcamera.viewer.ViewerFragment
import com.ebnbin.windowcamera.viewer.ViewerItem
import com.ebnbin.windowcamera.viewer.ViewerType
import com.jeremyliao.liveeventbus.LiveEventBus

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

    private lateinit var binding: Album2FragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = Album2FragmentBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.recyclerView.layoutManager = layoutManager

        adapter = AlbumAdapter()
        adapter.setOnItemClickListener { _, _, position ->
            when (adapter.data[position].multiSelect) {
                MultiSelect.NORMAL -> {
                    openFragment<ViewerFragment>(
                        fragmentArguments = ViewerFragment.createArguments(
                            adapter.data.map { ViewerItem(it.type, it.uri) },
                            position
                        ),
                        theme = R.style.EBAppTheme_Viewer,
                        fragmentActivityClass = ViewerActivity::class.java
                    )

//                    openFragment<ImageVideoFragment>(
//                        fragmentArguments = ImageVideoFragment.createArguments(ArrayList(adapter.data.map { ImageVideo(it.type, it.uri, it.index) }), position),
//                        theme = R.style.EBAppTheme_Viewer,
//                        fragmentActivityClass = ViewerActivity::class.java
//                    )
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
        adapter.setEmptyView(R.layout.album2_empty, binding.recyclerView)
        binding.recyclerView.adapter = adapter

        invalidateAlbumItems()
        exitActionMode()
    }

    private fun invalidateAlbumItems() {
        IOHelper.refreshFiles()
        val albumItems = ArrayList<AlbumItem>()
        IOHelper.files.forEachIndexed { index, file ->
            val type = when (file.name.substringAfterLast(".", "").toLowerCase()) {
                "jpg" -> ViewerType.IMAGE
                "mp4", "3gp" -> ViewerType.VIDEO
                else -> return@forEachIndexed
            }
            albumItems.add(AlbumItem(type, file.toUri(), index))
        }
        adapter.setNewData(albumItems)
    }

    private fun isActionMode(): Boolean {
        return onBackPressedCallback.isEnabled
    }

    private fun enterActionMode() {
        onBackPressedCallback.isEnabled = true

        binding.toolbar.setNavigationIcon(R.drawable.album2_close)
        binding.toolbar.setNavigationOnClickListener {
            multiSelectNormal()
            exitActionMode()
        }
        binding.toolbar.setTitleTextColor(ResHelper.getColorAttr(requireContext(), R.attr.colorPrimary))
        binding.toolbar.menu.clear()
        binding.toolbar.inflateMenu(R.menu.album2_action_mode)
        binding.toolbar.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.delete -> {
                    if (adapter.data.none { albumItem -> albumItem.multiSelect == MultiSelect.SELECTED }) {
                        requireContext().toast(R.string.album2_selected_empty)
                    } else {
                        childFragmentManager.openAlertDialogFragment(
                            message = getString(R.string.album2_delete_message),
                            positiveText = getString(R.string.album2_delete_position),
                            negativeText = getString(R.string.album2_delete_negative),
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
        binding.toolbar.title = getString(R.string.album2_selected, selectedCount)
    }

    private fun exitActionMode() {
        onBackPressedCallback.isEnabled = false

        binding.toolbar.setNavigationIcon(R.drawable.eb_toolbar_back)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding.toolbar.setTitle(R.string.album2)
        binding.toolbar.setTitleTextColor(ResHelper.getColorAttr(requireContext(), android.R.attr.textColorPrimary))
        binding.toolbar.menu.clear()
        binding.toolbar.inflateMenu(R.menu.album2_toolbar)
        binding.toolbar.setOnMenuItemClickListener {
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
            .forEach { it.uri.toFile().delete() }
        invalidateAlbumItems()

        multiSelectNormal()
        exitActionMode()
        return true
    }
}
