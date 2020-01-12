package com.ebnbin.eb.dialog

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.ebnbin.eb.fragment.FragmentCallback

/**
 * 打开 AlertDialog.
 */
fun FragmentManager.openAlertDialogFragment(
    isMaterial: Boolean = false,
    themeId: Int = 0,
    title: CharSequence? = null,
    message: CharSequence? = null,
    positiveText: CharSequence? = null,
    negativeText: CharSequence? = null,
    neutralText: CharSequence? = null,
    dialogCancelable: DialogCancelable = DialogCancelable.CANCELABLE,
    fragmentCallback: FragmentCallback = FragmentCallback.PREFER_PARENT_FRAGMENT,
    callbackBundle: Bundle = Bundle.EMPTY,
    fragmentTag: String? = null
) {
    commit(true) {
        add(
            AlertDialogFragment::class.java,
            AlertDialogFragment.createArguments(
                isMaterial = isMaterial,
                themeId = themeId,
                title = title,
                message = message,
                positiveText = positiveText,
                negativeText = negativeText,
                neutralText = neutralText,
                dialogCancelable = dialogCancelable,
                fragmentCallback = fragmentCallback,
                callbackBundle = callbackBundle
            ),
            fragmentTag
        )
    }
}

fun FragmentManager.openJsonApiDialogFragment(
    isMaterial: Boolean = false,
    themeId: Int = 0,
    request: CharSequence? = null,
    dialogCancelable: DialogCancelable = DialogCancelable.NOT_CANCELABLE_ON_TOUCH_OUTSIDE,
    fragmentCallback: FragmentCallback = FragmentCallback.PREFER_PARENT_FRAGMENT,
    callbackBundle: Bundle = Bundle.EMPTY,
    fragmentTag: String? = null
) {
    commit(true) {
        add(
            JsonApiDialogFragment::class.java,
            JsonApiDialogFragment.createArguments(
                isMaterial = isMaterial,
                themeId = themeId,
                request = request,
                dialogCancelable = dialogCancelable,
                fragmentCallback = fragmentCallback,
                callbackBundle = callbackBundle
            ),
            fragmentTag
        )
    }
}
