package com.ebnbin.windowcamera.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.ebnbin.eb.about.AboutFragment
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.eb.update.UpdateFragment
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.eb.util.res
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.CameraState
import com.ebnbin.windowcamera.profile.CameraStateEvent
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.profile.enumeration.Profile
import com.ebnbin.windowcamera.service.WindowCameraService
import com.ebnbin.windowcamera.service.WindowCameraServiceEvent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_fragment.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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
        if (savedInstanceState == null) {
            UpdateFragment.start(childFragmentManager, true)
        }

        bottom_app_bar.setNavigationOnClickListener {
            IntentHelper.startFragmentFromFragment(this, AboutFragment.intent())
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

        invalidateWindowCameraServiceEvent(WindowCameraService.isRunning())
        invalidateCameraState(ProfileHelper.cameraState)

        if (savedInstanceState == null) {
            showTip()
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

    override fun onPermissionsResult(permissions: ArrayList<String>, granted: Boolean, extraData: Bundle) {
        if (granted) {
            floating_action_button.isEnabled = false
            WindowCameraService.start(requireContext())
        }
    }

    override val isEventBusEnabled: Boolean = true

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: WindowCameraServiceEvent) {
        invalidateWindowCameraServiceEvent(event.isRunning)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CameraStateEvent) {
        invalidateCameraState(event.cameraState)
    }

    private fun invalidateWindowCameraServiceEvent(isWindowCameraServiceRunning: Boolean) {
        val imageDrawableId: Int
        val onClickListener: View.OnClickListener
        if (isWindowCameraServiceRunning) {
            imageDrawableId = R.drawable.main_stop
            onClickListener = View.OnClickListener {
                floating_action_button.isEnabled = false
                WindowCameraService.stop(requireContext())
            }
        } else {
            imageDrawableId = R.drawable.main_camera
            onClickListener = View.OnClickListener {
                PermissionFragment.start(childFragmentManager, WindowCameraService.permissions)
            }
        }
        floating_action_button.setImageResource(imageDrawableId)
        floating_action_button.setOnClickListener(onClickListener)

        spinner.isEnabled = !isWindowCameraServiceRunning
    }

    private fun invalidateCameraState(cameraState: CameraState) {
        floating_action_button.isEnabled = cameraState == CameraState.CLOSED ||
                cameraState == CameraState.PREVIEWING_PHOTO ||
                cameraState == CameraState.PREVIEWING_VIDEO ||
                cameraState == CameraState.PREVIEWING ||
                cameraState == CameraState.CAPTURING_VIDEO
    }

    //*****************************************************************************************************************

    private fun showTip() {
        if (tipShown) return
        tipShown = true
        floating_action_button.post {
            val tip = "${getString(R.string.main_tip_title)}${TIPS[Random.nextInt(TIPS.size)]}"
            val snackbar = Snackbar.make(floating_action_button, tip, Snackbar.LENGTH_INDEFINITE)
                .setAnchorView(floating_action_button)
            snackbar.setAction(R.string.main_tip_ok) {
                snackbar.dismiss()
            }
            snackbar.view.findViewById<TextView>(R.id.snackbar_text).maxLines = 4
            snackbar.show()
        }
    }

    //*****************************************************************************************************************

    override val isDoubleBackFinishEnabled: Boolean = true

    //*****************************************************************************************************************

    companion object {
        private val TIPS: Array<String> = res.getStringArray(R.array.main_tips)

        private var tipShown: Boolean = false
    }
}
