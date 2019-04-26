package com.ebnbin.windowcamera.main

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.ebnbin.eb.app.EBFragment
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.Consts
import com.ebnbin.eb.util.ResHelper
import com.ebnbin.eb.util.ebApp
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.app.WindowCameraService
import com.ebnbin.windowcamera.app.WindowCameraServiceEvent
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.menu.MenuDialogFragment
import com.ebnbin.windowcamera.profile.ProfileHelper
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
        PermissionFragment.start(childFragmentManager, arrayListOf(Manifest.permission.CAMERA), hashMapOf(
            Consts.CALLING_ID to "CameraHelper"
        ))
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

    override fun onPermissionsResult(permissions: ArrayList<String>, granted: Boolean, extraData: HashMap<*, *>) {
        when (extraData[Consts.CALLING_ID]) {
            "CameraHelper" -> {
                if (granted) {
                    asyncHelper.task(
                        {
                            if (!CameraHelper.isValid()) throw RuntimeException()
                        },
                        onSuccess = {
                            progress_bar.visibility = View.GONE
                            coordinator_layout.visibility = View.VISIBLE

                            mainPagerAdapter = MainPagerAdapter(requireContext(), childFragmentManager)
                            view_pager.adapter = mainPagerAdapter
                            view_pager.offscreenPageLimit = mainPagerAdapter.count - 1
                            view_pager.addOnPageChangeListener(this)
                            tab_layout.setupWithViewPager(view_pager)
                            bottom_app_bar.setNavigationOnClickListener {
                                MenuDialogFragment.start(childFragmentManager)
                            }
                            invalidateWindowCameraServiceEvent()

                            view_pager.setCurrentItem(ProfileHelper.page.value, false)
                        },
                        onFailure = {
                            when (it) {
                                is NoClassDefFoundError -> {
                                    // 在初始化 CameraHelper 时 task 中断导致.
                                    try {
                                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                            .setData(Uri.parse("package:${ebApp.packageName}"))
                                        startActivity(intent)
                                    } catch (e: ActivityNotFoundException) {
                                    }
                                    AppHelper.toast(requireContext(), R.string.camera_error_no_class_def_found_error)
                                    activity?.finish()
                                }
                                else -> {
                                    AppHelper.toast(requireContext(), R.string.camera_error)
                                    activity?.finish()
                                }
                            }
                        })
                } else {
                    activity?.finish()
                }
            }
            "WindowCameraService" -> {
                if (granted) {
                    WindowCameraService.start(requireContext())
                }
            }
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
                PermissionFragment.start(childFragmentManager, WindowCameraService.permissions, hashMapOf(
                    Consts.CALLING_ID to "WindowCameraService"
                ))
            }
        }
        floating_action_button.isEnabled = true
        floating_action_button.setImageResource(imageDrawableId)
        val backgroundTint = ResHelper.getColorAttr(requireContext(), backgroundTintAttrId)
        floating_action_button.backgroundTintList = ColorStateList.valueOf(backgroundTint)
        floating_action_button.setOnClickListener(onClickListener)
    }
}
