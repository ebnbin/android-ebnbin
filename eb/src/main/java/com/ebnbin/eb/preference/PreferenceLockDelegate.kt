package com.ebnbin.eb.preference

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceViewHolder
import com.ebnbin.eb.R
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb.sharedpreferences.SharedPreferencesProperty
import com.ebnbin.eb.util.AppHelper
import kotlin.math.abs

class PreferenceLockDelegate(private val callback: Callback) : View.OnTouchListener {
    fun onBindViewHolder(holder: PreferenceViewHolder?) {
        holder ?: return
        holder.itemView.foreground = if (isLocked) {
            callback.getContext().getDrawable(R.drawable.eb_preference_locked_foreground)
        } else {
            null
        }
        invalidateLock(holder.itemView)
        // SeekBar 需要特殊处理.
        invalidateLock(holder.itemView.findViewById(R.id.seekbar))
    }

    private fun invalidateLock(view: View?) {
        view ?: return
        view.setOnTouchListener(this)
        if (isLockable) {
            view.setOnLongClickListener {
                val cancelEvent = MotionEvent.obtain(
                    SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_CANCEL, 0f, 0f, 0
                )
                it.dispatchTouchEvent(cancelEvent)
                isLocked = true
                true
            }
        } else {
            view.setOnLongClickListener(null)
        }
    }

    //*****************************************************************************************************************

    private val unlockRunnable: Runnable = Runnable {
        AppHelper.vibrate(50L)
        isLocked = false
        isClick = false
    }

    private var downX: Float = 0f
    private var downY: Float = 0f

    private var isClick: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        v ?: return false
        event ?: return false
        if (!isLockable) return false
        if (!isLocked) return false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                isClick = true
                v.postDelayed(unlockRunnable, ViewConfiguration.getLongPressTimeout().toLong())
            }
            MotionEvent.ACTION_MOVE -> {
                val slop = ViewConfiguration.get(callback.getContext()).scaledTouchSlop
                if (abs(event.x - downX) > slop || abs(event.y - downY) > slop) {
                    v.removeCallbacks(unlockRunnable)
                }
            }
            MotionEvent.ACTION_UP -> {
                v.removeCallbacks(unlockRunnable)
                if (isClick) {
                    isClick = false
                    AppHelper.toast(callback.getContext(), R.string.eb_preference_locked)
                }
            }
            else -> {
                v.removeCallbacks(unlockRunnable)
            }
        }
        return true
    }

    //*****************************************************************************************************************

    var isLockable: Boolean = true
        set(value) {
            if (field == value) return
            field = value
            callback.notifyChanged()
        }

    fun setIsLockedDefaultValue(isLocked: Boolean) {
        val key = callback.getKey()
        val preferenceManager = callback.getPreferenceManager()
        if (key != null && preferenceManager != null) {
            if (!SharedPreferencesHelper.getSharedPreferences(preferenceManager.sharedPreferencesName)
                    .contains("${key}_is_locked")) {
                this.isLocked = isLocked
            }
        }
    }

    var isLocked: Boolean by SharedPreferencesProperty(
        {
            callback.getKey()?.let { "${it}_is_locked" }
        },
        { false },
        {
            val preferenceManager = callback.getPreferenceManager()
            if (preferenceManager == null) {
                null
            } else {
                SharedPreferencesHelper.getSharedPreferences(preferenceManager.sharedPreferencesName)
            }
        },
        { _, _ ->
            callback.notifyChanged()
            false
        }
    )

    //*****************************************************************************************************************

    interface Callback {
        fun getContext(): Context

        fun getPreferenceManager(): PreferenceManager?

        fun getKey(): String?

        fun notifyChanged()
    }
}
