package com.ebnbin.eb.dev

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.ebnbin.eb.R
import com.ebnbin.eb.databinding.EbDevItemViewBinding

@SuppressLint("ViewConstructor")
class DevItemView(
    context: Context,
    val title: CharSequence,
    summary: CharSequence? = null,
    onClick: ((DevItemView) -> Unit)? = null
) : FrameLayout(context), View.OnClickListener {
    val summary: MutableLiveData<CharSequence?> = MutableLiveData(summary)
    val onClick: MutableLiveData<((DevItemView) -> Unit)?> = MutableLiveData(onClick)

    override fun onClick(v: View?) {
        onClick.value?.invoke(this)
    }

    val binding: EbDevItemViewBinding = DataBindingUtil.inflate<EbDevItemViewBinding>(
        LayoutInflater.from(this.context), R.layout.eb_dev_item_view, this, true).also {
        it.view = this
    }
}
