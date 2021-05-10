package com.ebnbin.ebapp.update

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

fun FragmentManager.openUpdateFragment(
    type: UpdateFragment.Type = UpdateFragment.Type.MANUAL,
    fragmentTag: String? = null
) {
    commit(true) {
        add(
            UpdateFragment::class.java,
            UpdateFragment.createArguments(type),
            fragmentTag
        )
    }
}
