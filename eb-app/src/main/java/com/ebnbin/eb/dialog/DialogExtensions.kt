package com.ebnbin.eb.dialog

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.ebnbin.eb.DialogCancelable

fun FragmentManager.openAlertDialog(
    isMaterial: Boolean = false,
    title: CharSequence? = null,
    message: CharSequence? = null,
    positiveText: CharSequence? = null,
    negativeText: CharSequence? = null,
    neutralText: CharSequence? = null,
    dialogCancelable: DialogCancelable = DialogCancelable.CANCELABLE,
    callbackBundle: Bundle = Bundle.EMPTY,
    fragmentTag: String? = null
) {
    commit(true) {
        add(AlertDialogFragment::class.java, AlertDialogFragment.createArguments(
            isMaterial = isMaterial,
            title = title,
            message = message,
            positiveText = positiveText,
            negativeText = negativeText,
            neutralText = neutralText,
            dialogCancelable = dialogCancelable,
            callbackBundle = callbackBundle
        ), fragmentTag)
    }
}
