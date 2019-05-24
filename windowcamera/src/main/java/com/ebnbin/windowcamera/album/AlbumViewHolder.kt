package com.ebnbin.windowcamera.album

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ebnbin.windowcamera.R

class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView = itemView.findViewById(R.id.image_view)
}
