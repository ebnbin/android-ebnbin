package com.ebnbin.windowcamera.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.viewpager.widget.ViewPager
import com.ebnbin.eb.about.AboutFragment
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.eb.update.UpdateFragment
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.service.WindowCameraService
import com.ebnbin.windowcamera.service.WindowCameraServiceEvent
import kotlinx.android.synthetic.main.main_fragment.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainFragment : EBFragment(),
    AdapterView.OnItemSelectedListener,
    ViewPager.OnPageChangeListener,
    PermissionFragment.Callback
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            UpdateFragment.start(childFragmentManager, true)
        }

        bottom_app_bar.setNavigationOnClickListener {
            IntentHelper.startFragmentFromFragment(this, AboutFragment.intent())
        }
        spinner.adapter = MainSpinnerAdapter(bottom_app_bar.context)
        spinner.onItemSelectedListener = this
        tab_layout.setupWithViewPager(view_pager)
        view_pager.addOnPageChangeListener(this)

        view_pager.adapter = MainPagerAdapter(childFragmentManager)
        view_pager.offscreenPageLimit = MainPagerAdapter.ITEMS.size - 1

        if (savedInstanceState == null) {
            spinner.setSelection(
                MainSpinnerAdapter.ITEMS.indexOfFirst { it.first == ProfileHelper.profile.value }, false)
            view_pager.setCurrentItem(ProfileHelper.page.value, false)
        }

        invalidateWindowCameraServiceEvent()
    }

    override fun onDestroyView() {
        view_pager.removeOnPageChangeListener(this)
        super.onDestroyView()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (ProfileHelper.profile.value == MainSpinnerAdapter.ITEMS[position].first) return
        ProfileHelper.profile.value = MainSpinnerAdapter.ITEMS[position].first
        requireActivity().recreate()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
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
        val onClickListener: View.OnClickListener
        if (isWindowCameraServiceRunning) {
            imageDrawableId = R.drawable.main_stop
            onClickListener = View.OnClickListener {
                WindowCameraService.stop(requireContext())
            }
        } else {
            imageDrawableId = R.drawable.main_camera
            onClickListener = View.OnClickListener {
                PermissionFragment.start(childFragmentManager, WindowCameraService.permissions)
            }
        }
        floating_action_button.isEnabled = true
        floating_action_button.setImageResource(imageDrawableId)
        floating_action_button.setOnClickListener(onClickListener)

        spinner.isEnabled = !isWindowCameraServiceRunning
    }

    override val isDoubleBackFinishEnabled: Boolean = true
}
