package com.ebnbin.eb.bindingadapter

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("isVisible")
fun setVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
    view.adapter = adapter
}

@BindingAdapter("navigationOnClickListener")
fun setNavigationOnClickListener(toolbar: Toolbar, navigationOnClickListener: View.OnClickListener?) {
    toolbar.setNavigationOnClickListener(navigationOnClickListener)
}
