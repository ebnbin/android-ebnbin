package com.ebnbin.eb.bindingadapter

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun setVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

@BindingAdapter("navigationOnClickListener")
fun setNavigationOnClickListener(view: Toolbar, navigationOnClickListener: View.OnClickListener?) {
    view.setNavigationOnClickListener(navigationOnClickListener)
}
