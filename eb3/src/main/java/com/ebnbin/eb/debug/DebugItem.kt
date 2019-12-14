package com.ebnbin.eb.debug

import android.view.View
import androidx.lifecycle.MutableLiveData

class DebugItem<T>(
    val title: CharSequence,
    data: T,
    summary: CharSequence? = null,
    onClick: ((View, DebugItem<T>) -> Unit)? = null
) {
    val data: MutableLiveData<T> = MutableLiveData(data)
    val summary: MutableLiveData<CharSequence?> = MutableLiveData(summary)
    val onClick: MutableLiveData<View.OnClickListener?> = MutableLiveData(View.OnClickListener {
        onClick?.invoke(it, this)
    })
}
