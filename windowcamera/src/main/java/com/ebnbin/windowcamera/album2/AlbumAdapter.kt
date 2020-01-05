package com.ebnbin.windowcamera.album2

import android.view.View
import androidx.core.view.doOnLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.dpToPxRound
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.viewer.ViewerType

class AlbumAdapter : BaseMultiItemQuickAdapter<AlbumItem, BaseViewHolder>(null) {
    init {
        addItemType(ViewerType.IMAGE.ordinal, R.layout.album2_item_image)
        addItemType(ViewerType.VIDEO.ordinal, R.layout.album2_item_video)
    }

    override fun convert(helper: BaseViewHolder?, item: AlbumItem?) {
        helper ?: return
        item ?: return
        when (helper.itemViewType) {
            ViewerType.IMAGE.ordinal, ViewerType.VIDEO.ordinal -> {
                Glide.with(helper.itemView)
                    .load(item.uri)
                    .placeholder(R.drawable.album2_placeholder)
                    .into(helper.getView(R.id.image_view))
                when (item.multiSelect) {
                    MultiSelect.NORMAL -> {
                        helper.itemView.foreground = null
                        helper.getView<View>(R.id.image_view).doOnLayout {
                            it.scaleX = 1f
                            it.scaleY = 1f
                        }
                    }
                    MultiSelect.UNSELECTED -> {
                        helper.itemView.foreground = helper.itemView.context.getDrawable(R.drawable.album2_unselected)
                        helper.getView<View>(R.id.image_view).doOnLayout {
                            it.scaleX = 1f
                            it.scaleY = 1f
                        }
                    }
                    MultiSelect.SELECTED -> {
                        helper.itemView.foreground = helper.itemView.context.getDrawable(R.drawable.album2_selected)
                        helper.getView<View>(R.id.image_view).doOnLayout {
                            it.scaleX = (it.width - EBApplication.instance.dpToPxRound(16f)).toFloat() / it.width
                            it.scaleY = (it.height - EBApplication.instance.dpToPxRound(16f)).toFloat() / it.height
                        }
                    }
                }
            }
        }
    }
}
