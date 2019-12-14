package com.ebnbin.eb.debug

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.ebnbin.eb.R
import com.ebnbin.eb.databinding.EbDebugItemBinding

internal class DebugAdapter : RecyclerView.Adapter<DebugAdapter.DebugViewHolder>(), LifecycleOwner {
    val debugItems: ArrayList<DebugItem<*>> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebugViewHolder {
        return DebugViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.eb_debug_item,
            parent, false))
    }

    override fun onBindViewHolder(holder: DebugViewHolder, position: Int) {
        holder.binding.lifecycleOwner = this
        holder.binding.debugItem = debugItems[position]
    }

    override fun getItemCount(): Int {
        return debugItems.size
    }

    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this).also {
        it.currentState = Lifecycle.State.INITIALIZED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        super.onDetachedFromRecyclerView(recyclerView)
    }

    class DebugViewHolder(val binding: EbDebugItemBinding) : RecyclerView.ViewHolder(binding.root)
}
