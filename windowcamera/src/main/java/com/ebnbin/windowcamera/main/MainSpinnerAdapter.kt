package com.ebnbin.windowcamera.main

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.ThemedSpinnerAdapter
import com.ebnbin.eb.dpToPxRound
import com.ebnbin.eb2.util.ResHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.profile.enumeration.Profile

class MainSpinnerAdapter(context: Context) :
    ArrayAdapter<CharSequence>(context, R.layout.main_spinner_item, Profile.titles()) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getItem(position)
        textView.compoundDrawablePadding = context.dpToPxRound(8f)
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, Profile.get().iconId, 0)
        return view
    }

    private val helper = ThemedSpinnerAdapter.Helper(context)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: helper.dropDownViewInflater.inflate(R.layout.main_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getItem(position)
        textView.compoundDrawablePadding = context.dpToPxRound(8f)
        val drawable = context.getDrawable(Profile.get(position).iconId)
        if (drawable != null) {
            val attrId = if (Profile.get(position).key == ProfileHelper.profile.value) {
                R.attr.colorPrimary
            } else {
                android.R.attr.colorControlNormal
            }
            drawable.setTint(ResHelper.getColorAttr(context, attrId))
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
        }
        return view
    }

    override fun setDropDownViewTheme(theme: Resources.Theme?) {
        helper.dropDownViewTheme = theme
    }

    override fun getDropDownViewTheme(): Resources.Theme? {
        return helper.dropDownViewTheme
    }
}
