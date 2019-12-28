package com.ebnbin.eb.app2.binding

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun setVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}
