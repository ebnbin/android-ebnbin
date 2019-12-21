package com.ebnbin.windowcamera.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.ebnbin.eb.EBApp
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.eb.permission.openPermissionFragment
import com.ebnbin.eb2.dev.DevHelper
import com.ebnbin.eb2.fragment.EBFragment
import com.ebnbin.eb2.update.UpdateFragment
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.menu.MenuFragment
import com.ebnbin.windowcamera.profile.CameraState
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.profile.enumeration.Profile
import com.ebnbin.windowcamera.service.WindowCameraService
import com.ebnbin.windowcamera.util.SpManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.android.synthetic.main.main_fragment.*
import kotlin.random.Random

class MainFragment : EBFragment(),
    AdapterView.OnItemSelectedListener,
    ViewPager.OnPageChangeListener,
    PermissionFragment.Callback
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LiveEventBus.get("WindowCameraServiceEvent").observe(viewLifecycleOwner, Observer {
            val imageId: Int
            val listener: View.OnClickListener
            val isEnabled: Boolean
            if (WindowCameraService.isRunning()) {
                imageId = R.drawable.main_stop
                listener = View.OnClickListener {
                    floating_action_button.isEnabled = false
                    WindowCameraService.stop(requireContext())
                    DevHelper.reportEvent("window_camera_service_fab", bundleOf(
                        FirebaseAnalytics.Param.VALUE to "stop"
                    ))
                }
                isEnabled = false
            } else {
                imageId = R.drawable.main_camera
                listener = View.OnClickListener {
                    childFragmentManager.openPermissionFragment(WindowCameraService.permissions.toTypedArray())
                    DevHelper.reportEvent("window_camera_service_fab", bundleOf(
                        FirebaseAnalytics.Param.VALUE to "start"
                    ))
                }
                isEnabled = true
            }

            floating_action_button.setImageResource(imageId)
            floating_action_button.setOnClickListener(listener)
            spinner.isEnabled = isEnabled
        })
        LiveEventBus.get("CameraStateEvent").observe(viewLifecycleOwner, Observer {
            floating_action_button.isEnabled = ProfileHelper.cameraState != CameraState.STATING
        })

        if (savedInstanceState == null) {
            UpdateFragment.start(childFragmentManager, true)
        }

        bottom_app_bar.setNavigationOnClickListener {
            MenuFragment.start(childFragmentManager)
        }
        spinner.adapter = MainSpinnerAdapter(bottom_app_bar.context)
        spinner.onItemSelectedListener = this
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.isInlineLabel = true
        view_pager.offscreenPageLimit = MainPagerAdapter.ITEMS.size - 1
        view_pager.addOnPageChangeListener(this)

        if (savedInstanceState == null) {
            spinner.setSelection(Profile.indexOf(), false)
        }

        LiveEventBus.get("WindowCameraServiceEvent").post(Unit)
        LiveEventBus.get("CameraStateEvent").post(Unit)

        if (savedInstanceState == null) {
//            showTip()
        }
    }

    override fun onDestroyView() {
        view_pager.removeOnPageChangeListener(this)
        super.onDestroyView()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val changed = ProfileHelper.profile.value != Profile.get(position).key
        ProfileHelper.profile.value = Profile.get(position).key
        if (changed) {
            (spinner.adapter as MainSpinnerAdapter?)?.notifyDataSetChanged()
            DevHelper.reportEvent("profile_spinner", bundleOf(
                FirebaseAnalytics.Param.VALUE to ProfileHelper.profile.value
            ))
        }
        view_pager.adapter = MainPagerAdapter(childFragmentManager)
        (0 until tab_layout.tabCount).forEach {
            val tab = tab_layout.getTabAt(it) ?: return@forEach
            tab.setIcon(MainPagerAdapter.ITEMS[it].third)
        }
        view_pager.setCurrentItem(ProfileHelper.page.value, false)
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

    override fun onPermissionResult(permissions: Array<out String>, granted: Boolean, callbackBundle: Bundle) {
        super.onPermissionResult(permissions, granted, callbackBundle)
        if (granted) {
            floating_action_button.isEnabled = false
            WindowCameraService.start(requireContext())
        }
    }

    //*****************************************************************************************************************

    private fun showTip() {
        if (!SpManager.is_tip_enabled.value) return
        if (tipShown) return
        tipShown = true
        floating_action_button.post {
            val tip = "${getString(R.string.main_tip_title)}${TIPS[Random.nextInt(TIPS.size)]}"
            val snackbar = Snackbar.make(floating_action_button, tip, Snackbar.LENGTH_INDEFINITE)
                .setAnchorView(floating_action_button)
            snackbar.view.findViewById<TextView>(R.id.snackbar_text).maxLines = 4
            snackbar.show()
        }
    }

    //*****************************************************************************************************************

    override val isDoubleBackFinishEnabled: Boolean = true

    //*****************************************************************************************************************

    companion object {
        private val TIPS: Array<String> = EBApp.instance.resources.getStringArray(R.array.main_tips)

        private var tipShown: Boolean = false
    }
}
