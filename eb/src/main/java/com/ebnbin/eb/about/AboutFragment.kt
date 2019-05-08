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
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.IntentHelper
import kotlinx.android.synthetic.main.eb_about_fragment.*

class AboutFragment : EBFragment() {
    private lateinit var openSources: ArrayList<Pair<String, String>>
    private var bottomToTop: Boolean = false

    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        @Suppress("UNCHECKED_CAST")
        openSources = activityExtras.getSerializable("open_sources") as ArrayList<Pair<String, String>>
        bottomToTop = activityExtras.getBoolean("bottom_to_top")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.eb_about_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eb_toolbar.setNavigationIcon(if (bottomToTop) R.drawable.eb_toolbar_close else R.drawable.eb_toolbar_back)
        eb_toolbar.setNavigationOnClickListener {
            finish()
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
        eb_version.text = getString(R.string.eb_about_version, BuildHelper.versionName,
            if (debug) " ${BuildConfig.BUILD_TYPE}" else "")
        eb_update.setOnClickListener {
            UpdateFragment.start(childFragmentManager, false)
        }
        eb_open_market.setOnClickListener {
            IntentHelper.startMarket(requireContext())
        }
        OPEN_SOURCES.forEach {
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
            Pair("retrofit", "https://github.com/square/retrofit")
        )

        fun createIntent(
            openSources: ArrayList<Pair<String, String>> = arrayListOf(),
            bottomToTop: Boolean = false
        ): Intent {
            val result = Intent()
                .putExtra(EBActivity.KEY_FRAGMENT_CLASS, AboutFragment::class.java)
                .putExtra("open_sources", openSources)
                .putExtra("bottom_to_top", bottomToTop)
            if (bottomToTop) {
                result.putExtra(EBActivity.KEY_THEME_STYLE_ID, R.style.EBTheme_About)
            }
            return result
        }
    }
}
