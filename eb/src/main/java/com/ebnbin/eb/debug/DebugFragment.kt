package com.ebnbin.eb.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebnbin.eb.R
import com.ebnbin.eb.app.EBActivity
import com.ebnbin.eb.app.EBFragment
import kotlinx.android.synthetic.main.eb_debug_fragment.*

/**
 * Debug 页面.
 */
internal class DebugFragment : EBFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.eb_debug_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eb_view_pager.adapter = DebugPagerAdapter(childFragmentManager)
    }

    companion object {
        internal const val KEY_CALLING_FRAGMENT_CLASS_NAME = "calling_fragment_class_name"

        fun start(ebActivity: EBActivity) {
            EBActivity.startFragmentFromActivity(ebActivity, DebugFragment::class.java) {
                putExtra(KEY_CALLING_FRAGMENT_CLASS_NAME, ebActivity.fragmentClass?.name)
            }
        }
    }
}
