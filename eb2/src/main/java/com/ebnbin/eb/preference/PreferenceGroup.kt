package com.ebnbin.eb.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceViewHolder
import com.ebnbin.eb.R

open class PreferenceGroup(context: Context) :
    androidx.preference.PreferenceGroup(context, null),
    SharedPreferences.OnSharedPreferenceChangeListener {
    init {
        layoutResource = R.layout.eb_space
    }

    override fun onAttached() {
        super.onAttached()
        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        invalidateVisible()
    }

    override fun onDetached() {
        sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        super.onDetached()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        sharedPreferences ?: return
        key ?: return
        val visibleDependencyKeys = visibleDependencyKeys ?: return
        if (visibleDependencyKeys.contains(key)) invalidateVisible()
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        holder ?: return
        holder.isDividerAllowedAbove = false
        holder.isDividerAllowedBelow = false
    }

    //*****************************************************************************************************************

    private var visibleDependencyKeys: Array<out String>? = null
    private var visibleProvider: ((PreferenceGroup) -> Boolean?)? = null

    fun setVisibleProvider(
        vararg visibleDependencyKeys: String,
        visibleProvider: ((PreferenceGroup) -> Boolean?)? = null
    ) {
        this.visibleDependencyKeys = visibleDependencyKeys
        this.visibleProvider = visibleProvider
        invalidateVisible()
    }

    private fun invalidateVisible() {
        val isVisible = visibleProvider?.invoke(this) ?: return
        this.isVisible = isVisible
    }
}
