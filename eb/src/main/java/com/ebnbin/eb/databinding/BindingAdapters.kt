package com.ebnbin.eb.databinding

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter

@BindingAdapter("navigationOnClickListener")
fun setNavigationOnClickListener(view: Toolbar, navigationOnClickListener: View.OnClickListener?) {
    view.setNavigationOnClickListener(navigationOnClickListener)
}
