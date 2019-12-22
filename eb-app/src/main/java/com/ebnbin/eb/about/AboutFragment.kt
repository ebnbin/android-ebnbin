package com.ebnbin.eb.about

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.viewModels
import com.ebnbin.eb.databinding.EbAboutFragmentBinding
import com.ebnbin.eb.fragment.EBViewFragment

internal class AboutFragment : EBViewFragment<EbAboutFragmentBinding>() {
    private val viewModel: AboutFragmentViewModel by viewModels()

    override val bindingClass: Class<EbAboutFragmentBinding>
        get() = EbAboutFragmentBinding::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.setCloseOnClick {
            activity?.finish()
        }
        binding.setIconOnLongClick {
            it.isLongClickable = false
            it.animate()
                .rotationYBy(360f)
                .setInterpolator(AccelerateDecelerateInterpolator())
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
    }
}
