package com.ebnbin.eb.dev.floating

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.os.postDelayed
import androidx.core.view.isVisible
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.dev.DevFragment
import com.ebnbin.eb.dev.DevSpManager
import com.ebnbin.eb.fragment.FragmentActivity
import com.ebnbin.eb.fragment.openFragment
import com.ebnbin.eb.mainHandler

internal class DevFloatingOnAttachStateChangeListener(private val activity: Activity) :
    View.OnAttachStateChangeListener, DevFloatingView.Listener {
    private var popupWindow: PopupWindow? = null

    override fun onViewAttachedToWindow(v: View?) {
        val devFloatingView = DevFloatingView(activity)
        devFloatingView.setListener(this)
        val popupWindow = PopupWindow(devFloatingView)
        popupWindow.width = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.setBackgroundDrawable(null)
        this.popupWindow = popupWindow
        invalidatePopupWindow()
    }

    fun invalidatePopupWindow() {
        val popupWindow = popupWindow ?: return
        val x = DevSpManager.dev_floating_x.value
        val y = DevSpManager.dev_floating_y.value
        if (popupWindow.isShowing) {
            popupWindow.update(x, y, popupWindow.width, popupWindow.height)
        } else {
            popupWindow.showAtLocation(activity.window.decorView, Gravity.NO_GRAVITY, x, y)
        }
        popupWindow.contentView.isVisible = DevFloatingActivityLifecycleCallbacks.isFloatingVisible
    }

    override fun onViewDetachedFromWindow(v: View?) {
        popupWindow?.let {
            popupWindow = null
            it.dismiss()
        }
        activity.window.decorView.removeOnAttachStateChangeListener(this)
    }

    //*****************************************************************************************************************

    override fun onScroll(x: Int, y: Int) {
        super.onScroll(x, y)
        val popupWindow = popupWindow ?: return
        popupWindow.update(x, y, popupWindow.width, popupWindow.height)
        DevSpManager.dev_floating_x.value = x
        DevSpManager.dev_floating_y.value = y
    }

    override fun onSingleTap() {
        super.onSingleTap()
        if (activity is FragmentActivity && activity.fragmentClass == DevFragment::class.java) return
        activity.openFragment<DevFragment>(
            fragmentArguments = DevFragment.createArguments(
                devPages = EBApplication.instance.createDevPages.map { it.create(activity) }
            ),
            fragmentTag = DevFragment::class.java.name
        )
    }

    override fun onDoubleTap() {
        super.onDoubleTap()
        DevFloatingActivityLifecycleCallbacks.isFloatingVisible = false
        mainHandler.postDelayed(DevSpManager.dev_floating_hide_duration.value * 1000L) {
            DevFloatingActivityLifecycleCallbacks.isFloatingVisible = true
        }
    }
}
