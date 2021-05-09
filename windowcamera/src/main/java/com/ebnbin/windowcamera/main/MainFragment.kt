package com.ebnbin.windowcamera.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.eb.permission.openPermissionFragment
import com.ebnbin.eb2.dev.DevHelper
import com.ebnbin.eb2.fragment.EBFragment
import com.ebnbin.eb2.update.UpdateFragment
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.databinding.MainFragmentBinding
import com.ebnbin.windowcamera.menu.MenuFragment
import com.ebnbin.windowcamera.profile.CameraState
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.profile.enumeration.Profile
import com.ebnbin.windowcamera.service.WindowCameraService
import com.ebnbin.windowcamera.util.SpManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlin.random.Random

class MainFragment : EBFragment(),
    AdapterView.OnItemSelectedListener,
    ViewPager.OnPageChangeListener,
    PermissionFragment.Callback
{
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
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
                    binding.floatingActionButton.isEnabled = false
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

            binding.floatingActionButton.setImageResource(imageId)
            binding.floatingActionButton.setOnClickListener(listener)
            binding.spinner.isEnabled = isEnabled
        })
        LiveEventBus.get("CameraStateEvent").observe(viewLifecycleOwner, Observer {
            binding.floatingActionButton.isEnabled = ProfileHelper.cameraState != CameraState.STATING
        })

        if (savedInstanceState == null) {
            UpdateFragment.start(childFragmentManager, true)
        }

        binding.bottomAppBar.setNavigationOnClickListener {
            MenuFragment.start(childFragmentManager)
        }
        binding.spinner.adapter = MainSpinnerAdapter(binding.bottomAppBar.context)
        binding.spinner.onItemSelectedListener = this
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.tabLayout.isInlineLabel = true
        binding.viewPager.offscreenPageLimit = MainPagerAdapter.ITEMS.size - 1
        binding.viewPager.addOnPageChangeListener(this)

        if (savedInstanceState == null) {
            binding.spinner.setSelection(Profile.indexOf(), false)
        }

        view.post {
            LiveEventBus.get("WindowCameraServiceEvent").post(Unit)
            LiveEventBus.get("CameraStateEvent").post(Unit)
        }

        if (savedInstanceState == null) {
//            showTip()
        }
    }

    override fun onDestroyView() {
        binding.viewPager.removeOnPageChangeListener(this)
        super.onDestroyView()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val changed = ProfileHelper.profile.value != Profile.get(position).key
        ProfileHelper.profile.value = Profile.get(position).key
        if (changed) {
            (binding.spinner.adapter as MainSpinnerAdapter?)?.notifyDataSetChanged()
            DevHelper.reportEvent("profile_spinner", bundleOf(
                FirebaseAnalytics.Param.VALUE to ProfileHelper.profile.value
            ))
        }
        binding.viewPager.adapter = MainPagerAdapter(childFragmentManager)
        (0 until binding.tabLayout.tabCount).forEach {
            val tab = binding.tabLayout.getTabAt(it) ?: return@forEach
            tab.setIcon(MainPagerAdapter.ITEMS[it].third)
        }
        binding.viewPager.setCurrentItem(ProfileHelper.page.value, false)
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

    override fun onPermissionResult(
        context: Context,
        result: PermissionFragment.Result,
        deniedPermission: String?,
        callbackBundle: Bundle
    ): CharSequence? {
        if (result == PermissionFragment.Result.GRANTED) {
            binding.floatingActionButton.isEnabled = false
            WindowCameraService.start(requireContext())
        }
        return super.onPermissionResult(context, result, deniedPermission, callbackBundle)
    }

    //*****************************************************************************************************************

    private fun showTip() {
        if (!SpManager.is_tip_enabled.value) return
        if (tipShown) return
        tipShown = true
        binding.floatingActionButton.post {
            val tip = "${getString(R.string.main_tip_title)}${TIPS[Random.nextInt(TIPS.size)]}"
            val snackbar = Snackbar.make(binding.floatingActionButton, tip, Snackbar.LENGTH_INDEFINITE)
                .setAnchorView(binding.floatingActionButton)
            snackbar.view.findViewById<TextView>(R.id.snackbar_text).maxLines = 4
            snackbar.show()
        }
    }

    //*****************************************************************************************************************

    override fun onAttach(context: Context) {
        super.onAttach(context)
        moveTaskToBackOnBackPressedCallback.isEnabled = false
        doubleBackFinishOnBackPressedCallback.isEnabled = true
        disableBackFinishOnBackPressedCallback.isEnabled = false
    }

    //*****************************************************************************************************************

    companion object {
        private val TIPS: Array<String> = EBApplication.instance.resources.getStringArray(R.array.main_tips)

        private var tipShown: Boolean = false
    }
}
