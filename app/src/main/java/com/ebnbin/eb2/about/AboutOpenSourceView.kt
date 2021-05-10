package com.ebnbin.eb2.about

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ebnbin.eb.openBrowser
import com.ebnbin.windowcamera.databinding.EbAboutOpenSourceViewBinding

/**
 * 关于页面开源 item.
 */
internal class AboutOpenSourceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val binding: EbAboutOpenSourceViewBinding = EbAboutOpenSourceViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setData(pair: Pair<String, String>) {
        binding.ebName.text = pair.first
        binding.ebUrl.text = pair.second
        setOnClickListener {
            context.openBrowser(pair.second)
        }
    }
}
