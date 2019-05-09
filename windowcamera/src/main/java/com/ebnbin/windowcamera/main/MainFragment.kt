package com.ebnbin.windowcamera.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.ebnbin.eb.about.AboutFragment
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.eb.update.UpdateFragment
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.eb.util.ResHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.service.WindowCameraService
import com.ebnbin.windowcamera.service.WindowCameraServiceEvent
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
        if (savedInstanceState == null) {
            UpdateFragment.start(childFragmentManager, true)
        }

        mainPagerAdapter = MainPagerAdapter(requireContext(), childFragmentManager)
        view_pager.adapter = mainPagerAdapter
        view_pager.offscreenPageLimit = mainPagerAdapter.count - 1
        view_pager.addOnPageChangeListener(this)
        tab_layout.setupWithViewPager(view_pager)
        bottom_app_bar.setNavigationOnClickListener {
            IntentHelper.startFragmentFromFragment(this, AboutFragment.intent())
        }
        invalidateWindowCameraServiceEvent()

        if (savedInstanceState == null) {
            view_pager.setCurrentItem(ProfileHelper.page.value, false)
        }
    }

    override fun onDestroyView() {
        view_pager.removeOnPageChangeListener(this)
        super.onDestroyView()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        ProfileHelper.page.value = position
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPermissionsResult(permissions: ArrayList<String>, granted: Boolean, extraData: Bundle) {
        if (granted) {
            WindowCameraService.start(requireContext())
        }
    }

    override val isEventBusEnabled: Boolean = true

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: WindowCameraServiceEvent) {
        invalidateWindowCameraServiceEvent(event.isRunning)
    }

    private fun invalidateWindowCameraServiceEvent(
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
                PermissionFragment.start(childFragmentManager, WindowCameraService.permissions)
            }
        }
        floating_action_button.isEnabled = true
        floating_action_button.setImageResource(imageDrawableId)
        val backgroundTint = ResHelper.getColorAttr(requireContext(), backgroundTintAttrId)
        floating_action_button.backgroundTintList = ColorStateList.valueOf(backgroundTint)
        floating_action_button.setOnClickListener(onClickListener)
    }

    override val isDoubleBackFinishEnabled: Boolean = true
}
