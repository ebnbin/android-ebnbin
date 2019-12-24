package com.ebnbin.eb.permission

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

fun FragmentManager.openPermissionFragment(
    permissions: Array<out String>,
    callbackBundle: Bundle = Bundle.EMPTY,
    fragmentTag: String? = null
) {
    commit(true) {
        add(PermissionFragment::class.java, PermissionFragment.createArguments(
            permissions = permissions,
            callbackBundle = callbackBundle
        ), fragmentTag)
    }
}
