package com.ebnbin.windowcamera.main

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.ThemedSpinnerAdapter
import com.ebnbin.eb.util.res
import com.ebnbin.windowcamera.R

class MainSpinnerAdapter(context: Context) :
    ArrayAdapter<CharSequence>(context, R.layout.main_spinner_item, ITEMS.map { it.second }) {
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

    companion object {
        val ITEMS: List<Pair<String, CharSequence>> = listOf(
            Pair("default", res.getString(R.string.profile_title_default)),
            Pair("walking", res.getString(R.string.profile_title_walking)),
            Pair("custom_1", res.getString(R.string.profile_title_custom_1)),
            Pair("custom_2", res.getString(R.string.profile_title_custom_2))
        )
    }
}
