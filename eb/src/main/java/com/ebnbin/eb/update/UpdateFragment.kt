package com.ebnbin.eb.update

import android.os.Bundle
import com.ebnbin.eb.app.EBFragment
import com.ebnbin.eb.net.NetHelper
import io.reactivex.functions.Consumer

class UpdateFragment : EBFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            asyncRequest(
                NetHelper.ebService.update(),
                Consumer {
                    if (it.hasUpdate()) {
                        UpdateDialogFragment.start(childFragmentManager, it)
                    }
                }
            )
        }
    }
}
