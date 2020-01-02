package com.ebnbin.eb2.about

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.copy
import com.ebnbin.eb.fragment.getArgument
import com.ebnbin.eb.toast
import com.ebnbin.eb.versionName
import com.ebnbin.eb2.fragment.EBFragment
import com.ebnbin.eb2.update.UpdateFragment
import com.ebnbin.eb2.util.IntentHelper
import com.ebnbin.eb2.util.TimeHelper
import com.ebnbin.ebapp.deviceId
import com.ebnbin.windowcamera.R
import kotlinx.android.synthetic.main.eb_about_fragment2.*

class AboutFragment : EBFragment() {
    private val openSources: ArrayList<Pair<String, String>>?
        get() = getArgument("open_sources")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.eb_about_fragment2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eb_toolbar.setNavigationOnClickListener {
            activity?.finish()
        }
        eb_toolbar.setOnLongClickListener {
            requireContext().copy(requireContext().deviceId)
            requireContext().toast(R.string.eb_about_device_id_copied)
            true
        }
        eb_icon.setOnLongClickListener {
            it.isLongClickable = false
            it.animate()
                .rotationYBy(360f)
                .setDuration(1000L)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        it.rotationY = 0f
                        it.isLongClickable = true
                    }
                })
                .start()
            true
        }
        eb_version.text = getString(R.string.eb_about_version,
            EBApplication.instance.versionName,
            if (BuildConfig.DEBUG) " ${BuildConfig.BUILD_TYPE}" else "",
            TimeHelper.longToString(BuildConfig.BUILD_TIMESTAMP, "yyyy-MM-dd"))
        eb_update.setOnClickListener {
            UpdateFragment.start(childFragmentManager, false)
        }
        eb_open_market.setOnClickListener {
            IntentHelper.startMarket(requireContext())
        }
        OPEN_SOURCES.forEach {
            addOpenSource(it)
        }
        openSources?.forEach {
            addOpenSource(it)
        }
    }

    private fun addOpenSource(pair: Pair<String, String>) {
        val aboutOpenSourceView = AboutOpenSourceView(requireContext())
        aboutOpenSourceView.setData(pair)
        eb_open_source_linear_layout.addView(aboutOpenSourceView)
    }

    companion object {
        private val OPEN_SOURCES = listOf(
            Pair("kotlin", "https://github.com/JetBrains/kotlin"),
            Pair("AndroidX", "https://developer.android.com/jetpack/androidx"),
            Pair("Firebase", "https://firebase.google.com/"),
            Pair("CustomActivityOnCrash", "https://github.com/Ereza/CustomActivityOnCrash"),
            Pair("gson", "https://github.com/google/gson"),
            Pair("RxJava", "https://github.com/ReactiveX/RxJava"),
            Pair("RxAndroid", "https://github.com/ReactiveX/RxAndroid"),
            Pair("okhttp", "https://github.com/square/okhttp"),
            Pair("retrofit", "https://github.com/square/retrofit"),
            Pair("glide", "https://github.com/bumptech/glide"),
            Pair("BaseRecyclerViewAdapterHelper", "https://github.com/CymChad/BaseRecyclerViewAdapterHelper"),
            Pair("PhotoView", "https://github.com/chrisbanes/PhotoView"),
            Pair("GSYVideoPlayer", "https://github.com/CarGuo/GSYVideoPlayer")
        )
//
//        fun intent(openSources: ArrayList<Pair<String, String>> = arrayListOf()): Intent {
//            return Intent()
//                .putExtra(EBActivity.KEY_FRAGMENT_CLASS, AboutFragment::class.java)
//                .putExtra("open_sources", openSources)
//        }
    }
}
