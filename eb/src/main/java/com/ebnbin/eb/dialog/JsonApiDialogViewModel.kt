package com.ebnbin.eb.dialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

internal class JsonApiDialogViewModel : ViewModel() {
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val response: MutableLiveData<String?> = MutableLiveData()
}
