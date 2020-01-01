package com.ebnbin.eb.preference

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import androidx.preference.SwitchPreferenceCompat
import com.ebnbin.eb.sharedpreferences.get

open class SwitchPreference(context: Context) :
    SwitchPreferenceCompat(context),
    SharedPreferences.OnSharedPreferenceChangeListener {
    init {
        isSingleLineTitle = false
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
        sharedPreferences ?: return
        key ?: return
        if (this.key != key) return
        val value = sharedPreferences.get(key, isChecked)
        invalidateIcons(value)
        if (isChecked == value) return
        isChecked = value
    }

    //*****************************************************************************************************************

    private var icons: Pair<Drawable?, Drawable?>? = null
    private var iconIds: Pair<Int, Int>? = null

    fun setIcons(iconOff: Drawable?, iconOn: Drawable?) {
        val icons = Pair(iconOff, iconOn)
        if (this.icons == icons) return
        this.icons = icons
        iconIds = null
        invalidateIcons()
    }

    fun setIcons(iconOffId: Int, iconOnId: Int) {
        val iconIds = Pair(iconOffId, iconOnId)
        if (this.iconIds == iconIds) return
        this.iconIds = iconIds
        icons = null
        invalidateIcons()
    }

    private fun invalidateIcons(isChecked: Boolean = this.isChecked) {
        val icons = icons
        if (icons != null) {
            super.setIcon(if (isChecked) icons.second else icons.first)
            return
        }
        val iconIds = iconIds
        if (iconIds != null) {
            super.setIcon(if (isChecked) iconIds.second else iconIds.first)
        }
    }

    @Deprecated("Use setIcons instead.", ReplaceWith(""))
    override fun setIcon(icon: Drawable?) {
        super.setIcon(icon)
    }

    @Deprecated("Use setIcons instead.", ReplaceWith(""))
    override fun setIcon(iconResId: Int) {
        super.setIcon(iconResId)
    }
}
