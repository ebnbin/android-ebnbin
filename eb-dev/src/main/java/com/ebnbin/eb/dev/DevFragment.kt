package com.ebnbin.eb.dev

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ebnbin.eb.dev.databinding.EbDevFragmentBinding
import com.ebnbin.eb.requireArgument

internal class DevFragment : Fragment() {
    internal val callingActivity: CharSequence
        get() = requireArgument(KEY_CALLING_ACTIVITY)

    private lateinit var binding: EbDevFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = EbDevFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.setCloseOnClick {
            activity?.finish()
        }
    }

    companion object {
        private const val KEY_CALLING_ACTIVITY: String = "calling_activity"

        fun createArguments(
            callingActivity: CharSequence
        ): Bundle {
            return bundleOf(
                KEY_CALLING_ACTIVITY to callingActivity
            )
        }
    }
}
