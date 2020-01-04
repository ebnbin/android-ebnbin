package com.ebnbin.windowcamera.viewer

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ebnbin.eb.fragment.requireArgument

abstract class ViewerPageFragment : Fragment() {
    protected val viewerItem: ViewerItem
        get() = requireArgument(KEY_VIEWER_ITEM)

    companion object {
        private const val KEY_VIEWER_ITEM: String = "viewer_item"

        fun createArguments(viewerItem: ViewerItem): Bundle {
            return bundleOf(
                KEY_VIEWER_ITEM to viewerItem
            )
        }
    }
}
