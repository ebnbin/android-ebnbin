package com.ebnbin.eb.preference

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.preference.SwitchPreferenceCompat
import com.ebnbin.eb.R

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
    var iconOff: Int = 0
        set(value) {
            if (field == value) return
            field = value
            invalidateIcons()
        }

    var iconOn: Int = 0
        set(value) {
            if (field == value) return
            field = value
            invalidateIcons()
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EBSimpleSwitchPreference, defStyleAttr,
            defStyleRes)
        iconOff = typedArray.getResourceId(R.styleable.EBSimpleSwitchPreference_ebIconOff, 0)
        iconOn = typedArray.getResourceId(R.styleable.EBSimpleSwitchPreference_ebIconOn, 0)
        typedArray.recycle()
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
        if (isChecked) {
            if (iconOn == 0) {
                super.setIcon(null)
            } else {
                super.setIcon(iconOn)
            }
        } else {
            if (iconOff == 0) {
                super.setIcon(null)
            } else {
                super.setIcon(iconOff)
            }
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
