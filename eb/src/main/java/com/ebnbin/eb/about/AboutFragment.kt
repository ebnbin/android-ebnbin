package com.ebnbin.eb.about

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.R
import com.ebnbin.eb.app.EBFragment
import com.ebnbin.eb.debug.debug
import com.ebnbin.eb.update.UpdateFragment
import com.ebnbin.eb.util.BuildHelper
import kotlinx.android.synthetic.main.eb_about_fragment.*

class AboutFragment : EBFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.eb_about_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eb_toolbar.setNavigationOnClickListener {
            activity?.finish()
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
        eb_request_update.setOnClickListener {
            UpdateFragment.start(childFragmentManager, false)
        }
        OPEN_SOURCES.forEach {
            addOpenSource(it)
        }
        eb_share.setOnClickListener {
            // TODO
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
    }
}
