package com.ebnbin.windowcamera.main

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.ThemedSpinnerAdapter
import com.ebnbin.windowcamera.R

class MainSpinnerAdapter(context: Context, list: List<CharSequence>) :
    ArrayAdapter<CharSequence>(context, R.layout.main_spinner_item, list) {
    private val helper = ThemedSpinnerAdapter.Helper(context)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: helper.dropDownViewInflater.inflate(R.layout.main_spinner_item, parent, false)
        view.findViewById<TextView>(android.R.id.text1).text = getItem(position)
        return view
    }

    override fun setDropDownViewTheme(theme: Resources.Theme?) {
        helper.dropDownViewTheme = theme
    }

    override fun getDropDownViewTheme(): Resources.Theme? {
        return helper.dropDownViewTheme
    }
}
