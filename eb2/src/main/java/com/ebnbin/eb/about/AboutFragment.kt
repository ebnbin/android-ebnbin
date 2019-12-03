package com.ebnbin.eb.about

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.R
import com.ebnbin.eb.activity.EBActivity
import com.ebnbin.eb.debug.debug
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.update.UpdateFragment
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.DeviceHelper
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.eb.util.TimeHelper
import kotlinx.android.synthetic.main.eb_about_fragment.*

class AboutFragment : EBFragment() {
    private lateinit var openSources: ArrayList<Pair<String, String>>

    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        @Suppress("UNCHECKED_CAST")
        openSources = activityExtras.getSerializable("open_sources") as ArrayList<Pair<String, String>>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.eb_about_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eb_toolbar.setNavigationOnClickListener {
            finish()
        }
        eb_toolbar.setOnLongClickListener {
            AppHelper.copy(DeviceHelper.DEVICE_ID)
            AppHelper.toast(requireContext(), R.string.eb_about_device_id_copied)
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
            BuildHelper.versionName,
            if (debug) " ${BuildConfig.BUILD_TYPE}" else "",
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
        openSources.forEach {
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
            Pair("EventBus", "https://github.com/greenrobot/EventBus"),
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

        fun intent(openSources: ArrayList<Pair<String, String>> = arrayListOf()): Intent {
            return Intent()
                .putExtra(EBActivity.KEY_FRAGMENT_CLASS, AboutFragment::class.java)
                .putExtra("open_sources", openSources)
        }
    }
}
