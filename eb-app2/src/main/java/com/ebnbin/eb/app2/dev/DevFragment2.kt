package com.ebnbin.eb.app2.dev

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb.app2.about.AboutFragment
import com.ebnbin.eb.app2.databinding.EbDevFragment2Binding
import com.ebnbin.eb.app2.fragment.EBViewFragment
import com.ebnbin.eb.app2.loading.Loading
import com.ebnbin.eb.fragment.openFragment
import com.ebnbin.eb.toast
import kotlinx.coroutines.Job

/**
 * Dev 页面.
 */
open class DevFragment2 : EBViewFragment<EbDevFragment2Binding>() {
    private val viewModel: DevFragmentViewModel by viewModels()

    override val bindingClass: Class<EbDevFragment2Binding>
        get() = EbDevFragment2Binding::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setCloseOnClick {
            activity?.finish()
        }
        onAddDevItems()
    }

    @CallSuper
    protected open fun onAddDevItems() {
        addDevItem("About") {
            openFragment<AboutFragment>()
        }

        val coroutineDevItemView = addDevItem("Coroutine") {
            it.summary.value = null
            viewModel.sampleJson.coroutineSetValue()
        }
        viewModel.sampleJson.observe(viewLifecycleOwner, Observer {
            coroutineDevItemView.summary.value = it
        })
        viewModel.sampleJson.addLifecycleLoading(viewLifecycleOwner.lifecycle, object :
            Loading<String?> {
            override fun onStart(job: Job) {
                super.onStart(job)
                EBApp.instance.toast("onStart")
            }

            override fun onSuccess(result: String?) {
                super.onSuccess(result)
                EBApp.instance.toast("onSuccess")
            }

            override fun onFailure(throwable: Throwable) {
                super.onFailure(throwable)
                EBApp.instance.toast("onFailure")
            }
        })
    }

    protected fun addDevItem(
        title: CharSequence,
        summary: CharSequence? = null,
        onClick: ((DevItemView) -> Unit)? = null
    ): DevItemView {
        return DevItemView(requireContext(), title, summary, onClick).also {
            it.binding.lifecycleOwner = viewLifecycleOwner
            binding.ebLinearLayout.addView(it)
        }
    }
}
