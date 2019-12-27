package com.ebnbin.eb.dev

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

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
        val x = DevSpManager.floating_x.value
        val y = DevSpManager.floating_y.value
        if (popupWindow.isShowing) {
            popupWindow.update(x, y, popupWindow.width, popupWindow.height)
        } else {
            popupWindow.showAtLocation(activity.window.decorView, Gravity.NO_GRAVITY, x, y)
        }
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
        DevSpManager.floating_x.value = x
        DevSpManager.floating_y.value = y
    }
}
