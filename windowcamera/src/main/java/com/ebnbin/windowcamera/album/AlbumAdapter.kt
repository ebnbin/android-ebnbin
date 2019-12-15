package com.ebnbin.windowcamera.album

import android.view.View
import androidx.core.view.doOnLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ebnbin.eb.EBApp
import com.ebnbin.eb.extension.dpToPxRound
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.imagevideo.ImageVideo

class AlbumAdapter : BaseMultiItemQuickAdapter<AlbumItem, BaseViewHolder>(null) {
    init {
        addItemType(ImageVideo.Type.IMAGE.ordinal, R.layout.album_item_image)
        addItemType(ImageVideo.Type.VIDEO.ordinal, R.layout.album_item_video)
    }

    override fun convert(helper: BaseViewHolder?, item: AlbumItem?) {
        helper ?: return
        item ?: return
        when (helper.itemViewType) {
            ImageVideo.Type.IMAGE.ordinal, ImageVideo.Type.VIDEO.ordinal -> {
                Glide.with(helper.itemView)
                    .load(item.file)
                    .placeholder(R.drawable.album_placeholder)
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
                        helper.itemView.foreground = helper.itemView.context.getDrawable(R.drawable.album_unselected)
                        helper.getView<View>(R.id.image_view).doOnLayout {
                            it.scaleX = 1f
                            it.scaleY = 1f
                        }
                    }
                    MultiSelect.SELECTED -> {
                        helper.itemView.foreground = helper.itemView.context.getDrawable(R.drawable.album_selected)
                        helper.getView<View>(R.id.image_view).doOnLayout {
                            it.scaleX = (it.width - EBApp.instance.dpToPxRound(16f)).toFloat() / it.width
                            it.scaleY = (it.height - EBApp.instance.dpToPxRound(16f)).toFloat() / it.height
                        }
                    }
                }
            }
        }
    }
}
