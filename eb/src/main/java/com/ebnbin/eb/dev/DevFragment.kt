package com.ebnbin.eb.dev

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ebnbin.eb.EBApp
import com.ebnbin.eb.about.AboutFragment
import com.ebnbin.eb.crash.CrashException
import com.ebnbin.eb.databinding.EbDevFragmentBinding
import com.ebnbin.eb.dialog.openAlertDialog
import com.ebnbin.eb.fragment.EBViewFragment
import com.ebnbin.eb.fragment.openFragment
import com.ebnbin.eb.loading.Loading
import com.ebnbin.eb.widget.toast
import kotlinx.coroutines.Job

/**
 * Dev 页面.
 */
open class DevFragment : EBViewFragment<EbDevFragmentBinding>() {
    private val viewModel: DevFragmentViewModel by viewModels()

    override val bindingClass: Class<EbDevFragmentBinding>
        get() = EbDevFragmentBinding::class.java

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
        viewModel.sampleJson.addLifecycleLoading(viewLifecycleOwner.lifecycle, object : Loading<String?> {
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

        addDevItem("Report") {
            childFragmentManager.openAlertDialog(
                title = "Report",
                message = Report.create().toString(),
                negativeText = "取消"
            )
        }

        addDevItem("Calling Activity", arguments?.getString(KEY_CALLING_ACTIVITY))

        addDevItem("AlertDialogFragment", "isMaterial = false") {
            childFragmentManager.openAlertDialog(
                isMaterial = false,
                title = "Title",
                message = "Message",
                positiveText = "Positive",
                negativeText = "Negative"
            )
        }

        addDevItem("AlertDialogFragment", "isMaterial = true") {
            childFragmentManager.openAlertDialog(
                isMaterial = true,
                title = "Title",
                message = "Message",
                positiveText = "Positive",
                negativeText = "Negative"
            )
        }

        addDevItem("Toast") {
            Toast.makeText(requireContext(), "${System.currentTimeMillis()}", Toast.LENGTH_SHORT).show()
        }

        addDevItem("Simple Toast") {
            requireContext().toast("${System.currentTimeMillis()}")
        }

        addDevItem("Crash") {
            throw CrashException()
        }
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

    companion object {
        const val KEY_CALLING_ACTIVITY: String = "calling_activity"
    }
}
