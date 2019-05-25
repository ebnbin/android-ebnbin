package com.ebnbin.windowcamera.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.util.IOHelper

class AlbumAdapter : RecyclerView.Adapter<AlbumViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false)
        return AlbumViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(IOHelper.files[position])
            .into(holder.imageView)

        if (listener != null) {
            holder.itemView.setOnClickListener {
                listener?.onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return IOHelper.files.size
    }

    var listener: Listener? = null

    interface Listener {
        fun onItemClick(position: Int)
    }
}
