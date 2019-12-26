package com.ebnbin.eb2.preference

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.ebnbin.windowcamera.R

/**
 * 高度 100dp.
 */
open class FooterPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : Preference(context, attrs, defStyleAttr, defStyleRes) {
    init {
        layoutResource = R.layout.eb_footer_preference
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        holder ?: return
        holder.isDividerAllowedAbove = false
        holder.isDividerAllowedBelow = false
    }
}
