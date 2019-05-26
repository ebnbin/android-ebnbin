package com.ebnbin.windowcamera.album

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.imagevideo.ImageVideo

class AlbumAdapter : BaseMultiItemQuickAdapter<ImageVideo, BaseViewHolder>(null) {
    init {
        addItemType(ImageVideo.Type.IMAGE.ordinal, R.layout.album_item_image)
        addItemType(ImageVideo.Type.VIDEO.ordinal, R.layout.album_item_video)
    }

    override fun convert(helper: BaseViewHolder?, item: ImageVideo?) {
        helper ?: return
        item ?: return
        when (helper.itemViewType) {
            ImageVideo.Type.IMAGE.ordinal -> {
                Glide.with(helper.itemView)
                    .load(item.url)
                    .into(helper.getView(R.id.image_view))
            }
            ImageVideo.Type.VIDEO.ordinal -> {
                Glide.with(helper.itemView)
                    .load(item.url)
                    .into(helper.getView(R.id.image_view))
            }
        }
    }
}
