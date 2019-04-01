package com.ebnbin.windowcamera.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.viewpager.widget.ViewPager
import com.ebnbin.eb.app.EBFragment
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.eb.util.getColorAttr
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.event.WindowCameraServiceEvent
import com.ebnbin.windowcamera.menu.MenuDialogFragment
import com.ebnbin.windowcamera.service.WindowCameraService
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.main_fragment.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainFragment : EBFragment(), ViewPager.OnPageChangeListener, PermissionFragment.Callback {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    private lateinit var mainPagerAdapter: MainPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainPagerAdapter = MainPagerAdapter(requireContext(), childFragmentManager)
        view_pager.adapter = mainPagerAdapter
        view_pager.offscreenPageLimit = mainPagerAdapter.count - 1
        view_pager.addOnPageChangeListener(this)
        tab_layout.setupWithViewPager(view_pager)
        bottom_app_bar.setNavigationOnClickListener {
            MenuDialogFragment.start(childFragmentManager)
        }
        val list = listOf(
            getString(R.string.profile_default),
            getString(R.string.profile_custom_1),
            getString(R.string.profile_custom_2)
        )
        spinner.adapter = MainSpinnerAdapter(bottom_app_bar.context, list)
        invalidateFloatingActionButton()
    }

    override fun onDestroyView() {
        view_pager.removeOnPageChangeListener(this)
        super.onDestroyView()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        if (mainPagerAdapter.hideOnScroll(position)) {
            tab_layout.updateLayoutParams<AppBarLayout.LayoutParams> {
                scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
            }
            bottom_app_bar.hideOnScroll = true
        } else {
            tab_layout.updateLayoutParams<AppBarLayout.LayoutParams> {
                scrollFlags = 0
            }
            bottom_app_bar.hideOnScroll = false

            app_bar_layout.setExpanded(true, true)
            bottom_app_bar.slideUp()
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPermissionsResult(
        callingId: String,
        permissions: List<String>,
        granted: Boolean,
        extraData: Map<String, Any?>
    ) {
        if (!granted) return
        when (callingId) {
            "WindowCameraService" -> WindowCameraService.start(requireContext())
        }
    }

    override val isEventBusEnabled: Boolean = true

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: WindowCameraServiceEvent) {
        invalidateFloatingActionButton(event.isRunning)
    }

    private fun invalidateFloatingActionButton(
        isWindowCameraServiceRunning: Boolean = WindowCameraService.isRunning()) {
        val imageDrawableId: Int
        val backgroundTintAttrId: Int
        val onClickListener: View.OnClickListener
        if (isWindowCameraServiceRunning) {
            imageDrawableId = R.drawable.main_stop
            backgroundTintAttrId = R.attr.colorSecondary
            onClickListener = View.OnClickListener {
                WindowCameraService.stop(requireContext())
            }
        } else {
            imageDrawableId = R.drawable.main_camera
            backgroundTintAttrId = R.attr.colorPrimary
            onClickListener = View.OnClickListener {
                PermissionFragment.start(childFragmentManager, "WindowCameraService", WindowCameraService.permissions)
            }
        }
        floating_action_button.isEnabled = true
        floating_action_button.setImageResource(imageDrawableId)
        val backgroundTint = getColorAttr(requireContext(), backgroundTintAttrId)
        floating_action_button.backgroundTintList = ColorStateList.valueOf(backgroundTint)
        floating_action_button.setOnClickListener(onClickListener)
    }

    companion object {
        private fun BottomAppBar.slideUp() {
            (behavior as BottomAppBar.Behavior).slideUp(this)
        }
    }
}
