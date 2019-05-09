package com.ebnbin.eb.debug

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.ebnbin.eb.R
import com.ebnbin.eb.activity.EBActivity
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.sharedpreferences.EBSpManager
import com.ebnbin.eb.util.IntentHelper
import kotlinx.android.synthetic.main.eb_debug_fragment.*

/**
 * Debug 页面.
 */
internal class DebugFragment : EBFragment(), Toolbar.OnMenuItemClickListener, ViewPager.OnPageChangeListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.eb_debug_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eb_toolbar.setNavigationOnClickListener {
            finish()
        }
        eb_toolbar.inflateMenu(R.menu.eb_debug_toolbar)
        eb_toolbar.setOnMenuItemClickListener(this)
        eb_view_pager.adapter = DebugPagerAdapter(childFragmentManager)
        eb_view_pager.addOnPageChangeListener(this)

        val count = eb_view_pager.adapter?.count ?: 0
        eb_toolbar.menu.findItem(R.id.eb_switch)?.isVisible = count > 1
        if (savedInstanceState == null) eb_view_pager.currentItem = EBSpManager.debug_page.value
        if (eb_view_pager.currentItem == 0) onPageSelected(0)
    }

    override fun onDestroyView() {
        eb_view_pager.removeOnPageChangeListener(this)
        super.onDestroyView()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.eb_switch -> {
                val count = eb_view_pager.adapter?.count ?: 0
                if (count < 2) return true

                fun next(count: Int, current: Int): Int {
                    var next = current + 1
                    if (next >= count) next = 0
                    return next
                }

                val next = next(count, eb_view_pager.currentItem)
                eb_view_pager.setCurrentItem(next, true)
                return true
            }
            else -> return false
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        eb_toolbar.title = eb_view_pager.adapter?.getPageTitle(position)
        EBSpManager.debug_page.value = position
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    companion object {
        internal const val KEY_CALLING_ACTIVITY = "calling_activity"

        fun start(ebActivity: EBActivity): Boolean {
            val intent = Intent()
                .putExtra(EBActivity.KEY_THEME_STYLE_ID, R.style.EBTheme_Debug)
                .putExtra(EBActivity.KEY_FRAGMENT_CLASS, DebugFragment::class.java)
                .putExtra(KEY_CALLING_ACTIVITY, ebActivity.toString())
            return IntentHelper.startFragmentFromActivity(ebActivity, intent)
        }
    }
}
