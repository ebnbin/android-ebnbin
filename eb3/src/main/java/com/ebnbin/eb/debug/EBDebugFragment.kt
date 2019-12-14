package com.ebnbin.eb.debug

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import com.ebnbin.eb.R
import com.ebnbin.eb.about.AboutFragment
import com.ebnbin.eb.databinding.EbDebugFragmentBinding
import com.ebnbin.eb.extension.startFragmentByFragment
import com.ebnbin.eb.fragment.EBViewFragment

/**
 * Debug 页面.
 */
open class EBDebugFragment : EBViewFragment<EbDebugFragmentBinding>() {
    override val layoutId: Int = R.layout.eb_debug_fragment

    private lateinit var adapter: DebugAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DebugAdapter()
        binding.setBackOnClick {
            activity?.onBackPressed()
        }
        binding.adapter = adapter
        onAddDebugItems()
        adapter.notifyDataSetChanged()
    }

    @CallSuper
    protected open fun onAddDebugItems() {
        addDebugItem("Calling activity", arguments?.getString(KEY_CALLING_ACTIVITY).toString())

        addDebugItem("About") { _, _ ->
            startFragmentByFragment(AboutFragment::class.java)
        }

        addDebugItem("Crash") { _, _ ->
            throw RuntimeException()
        }
    }

    protected fun addDebugItem(
        title: CharSequence,
        summary: CharSequence? = null,
        onClick: ((View, DebugItem<Unit>) -> Unit)? = null
    ): DebugItem<Unit> {
        return addDebugItem(title, Unit, summary, onClick)
    }

    protected fun <T> addDebugItem(
        title: CharSequence,
        data: T,
        summary: CharSequence? = null,
        onClick: ((View, DebugItem<T>) -> Unit)? = null
    ): DebugItem<T> {
        return DebugItem(title, data, summary, onClick).also {
            adapter.debugItems.add(it)
        }
    }

    companion object {
        internal const val KEY_CALLING_ACTIVITY: String = "calling_activity"
    }
}
