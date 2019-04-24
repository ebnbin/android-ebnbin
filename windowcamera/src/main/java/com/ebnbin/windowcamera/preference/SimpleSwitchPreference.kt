package com.ebnbin.windowcamera.preference

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.preference.SwitchPreferenceCompat
import com.ebnbin.windowcamera.R

/**
 * 拥有 off/on 图标的 SwitchPreference.
 */
open class SimpleSwitchPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.switchPreferenceCompatStyle,
    defStyleRes: Int = 0
) : SwitchPreferenceCompat(context, attrs, defStyleAttr, defStyleRes),
    SharedPreferences.OnSharedPreferenceChangeListener {
    var icons: Pair<Int, Int>? = null
        set(value) {
            if (field == value) return
            field = value
            invalidateIcons()
        }

    @Deprecated("使用 icons 代替.", ReplaceWith(""))
    override fun setIcon(icon: Drawable?) {
        super.setIcon(icon)
    }

    @Deprecated("使用 icons 代替.", ReplaceWith(""))
    override fun setIcon(iconResId: Int) {
        super.setIcon(iconResId)
    }

    private fun invalidateIcons() {
        val icons = icons
        if (icons == null) {
            super.setIcon(null)
        } else {
            super.setIcon(if (isChecked) icons.second else icons.first)
        }
    }

    override fun onAttached() {
        super.onAttached()
        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        invalidateIcons()
    }

    override fun onDetached() {
        sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        super.onDetached()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key ?: return
        if (this.key == key) {
            invalidateIcons()
        }
    }
}
