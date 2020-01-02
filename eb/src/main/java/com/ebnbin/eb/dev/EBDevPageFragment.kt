package com.ebnbin.eb.dev

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.closeApp
import com.ebnbin.eb.crash.CrashException
import com.ebnbin.eb.dialog.AlertDialogFragment
import com.ebnbin.eb.dialog.openAlertDialogFragment
import com.ebnbin.eb.fragment.requireArgument
import com.ebnbin.eb.getValue
import com.ebnbin.eb.openBrowser
import com.ebnbin.eb.openMarket
import com.ebnbin.eb.preference.Preference
import com.ebnbin.eb.preference.PreferenceCategory
import com.ebnbin.eb.preference.SeekBarPreference
import com.ebnbin.eb.timestamp
import com.ebnbin.eb.toTimeString
import com.ebnbin.eb.toast

internal class EBDevPageFragment : PreferenceFragmentCompat(), AlertDialogFragment.Callback {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = DevSpManager.name
        preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //*************************************************************************************************************

        val callingActivityPreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Calling Activity"
        }

        Preference(requireContext()).also {
            callingActivityPreferenceGroup.addPreference(it)
            it.title = "Calling Activity"
            it.summary = requireArgument(KEY_CALLING_ACTIVITY)
        }

        //*************************************************************************************************************

        val floatingPreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Dev"
        }

        Preference(requireContext()).also {
            fun summary(
                floatingX: Int = DevSpManager.dev_floating_x.value,
                floatingY: Int = DevSpManager.dev_floating_y.value
            ): CharSequence {
                return "$floatingX,$floatingY"
            }

            floatingPreferenceGroup.addPreference(it)
            it.title = "悬浮按钮位置（像素）"
            it.summary = summary()
            DevSpManager.dev_floating_x.addOnChange(viewLifecycleOwner.lifecycle) { _, newValue ->
                it.summary = summary(floatingX = newValue)
                false
            }
            DevSpManager.dev_floating_y.addOnChange(viewLifecycleOwner.lifecycle) { _, newValue ->
                it.summary = summary(floatingY = newValue)
                false
            }
        }

        SeekBarPreference(requireContext()).also {
            it.key = DevSpManager.dev_floating_hide_duration.key
            it.setDefaultValue(DevSpManager.dev_floating_hide_duration.defaultValue)
            floatingPreferenceGroup.addPreference(it)
            it.title = "双击悬浮按钮临时隐藏时长（秒）"
            it.min = 1
            it.max = 60
            it.seekBarIncrement = 1
        }

        //*************************************************************************************************************

        val reportPreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Report"
        }

        Preference(requireContext()).also {
            reportPreferenceGroup.addPreference(it)
            it.title = "Report"
            it.setOnPreferenceClickListener {
                childFragmentManager.openAlertDialogFragment(
                    title = "Report",
                    message = EBApplication.instance.createReport().toString(),
                    negativeText = "取消",
                    fragmentTag = "Report"
                )
                true
            }
        }

        //*************************************************************************************************************

        val dialogPreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Dialog"
        }

        Preference(requireContext()).also {
            dialogPreferenceGroup.addPreference(it)
            it.title = "AlertDialogFragment"
            it.summary = "isMaterial = false"
            it.setOnPreferenceClickListener {
                childFragmentManager.openAlertDialogFragment(
                    isMaterial = false,
                    title = "Title",
                    message = (0 until 20).joinToString { i -> "Message $i" },
                    positiveText = "确定",
                    negativeText = "取消",
                    fragmentTag = "isMaterial = false"
                )
                true
            }
        }

        Preference(requireContext()).also {
            dialogPreferenceGroup.addPreference(it)
            it.title = "AlertDialogFragment"
            it.summary = "isMaterial = true"
            it.setOnPreferenceClickListener {
                childFragmentManager.openAlertDialogFragment(
                    isMaterial = true,
                    title = "Title",
                    message = (0 until 20).joinToString { i -> "Message $i" },
                    positiveText = "确定",
                    negativeText = "取消",
                    fragmentTag = "isMaterial = true"
                )
                true
            }
        }

        //*************************************************************************************************************

        val toastPreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Toast"
        }

        Preference(requireContext()).also {
            toastPreferenceGroup.addPreference(it)
            it.title = "普通 Toast"
            it.setOnPreferenceClickListener {
                Toast.makeText(requireContext(), timestamp().toTimeString(), Toast.LENGTH_SHORT).show()
                true
            }
        }

        Preference(requireContext()).also {
            toastPreferenceGroup.addPreference(it)
            it.title = "不重叠 Toast"
            it.setOnPreferenceClickListener {
                requireContext().toast(timestamp().toTimeString())
                true
            }
        }

        //*************************************************************************************************************

        val viewPreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "View"
        }

        Preference(requireContext()).also {
            viewPreferenceGroup.addPreference(it)
            it.title = "LONG_PRESS 震动反馈"
            it.setOnPreferenceClickListener {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                true
            }
        }

        //*************************************************************************************************************

        val intentPreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Intent"
        }

        Preference(requireContext()).also {
            intentPreferenceGroup.addPreference(it)
            it.title = "打开应用商店"
            it.summary = "googlePlayStore = false"
            it.setOnPreferenceClickListener {
                requireContext().openMarket(googlePlayStore = false)
                true
            }
        }

        Preference(requireContext()).also {
            intentPreferenceGroup.addPreference(it)
            it.title = "打开应用商店"
            it.summary = "googlePlayStore = true"
            it.setOnPreferenceClickListener {
                requireContext().openMarket(googlePlayStore = true)
                true
            }
        }

        Preference(requireContext()).also {
            intentPreferenceGroup.addPreference(it)
            it.title = "打开浏览器"
            it.summary = "chrome = false"
            it.setOnPreferenceClickListener {
                requireContext().openBrowser("https://github.com/ebnbin", chrome = false)
                true
            }
        }

        Preference(requireContext()).also {
            intentPreferenceGroup.addPreference(it)
            it.title = "打开浏览器"
            it.summary = "chrome = true"
            it.setOnPreferenceClickListener {
                requireContext().openBrowser("https://github.com/ebnbin", chrome = true)
                true
            }
        }

        //*************************************************************************************************************

        val appPreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "App"
        }

        Preference(requireContext()).also {
            appPreferenceGroup.addPreference(it)
            it.title = "关闭 App"
            it.summary = "killProcessDelay = -1L"
            it.setOnPreferenceClickListener {
                requireActivity().closeApp(reopenApp = false, killProcessDelay = -1L)
                true
            }
        }

        Preference(requireContext()).also {
            appPreferenceGroup.addPreference(it)
            it.title = "关闭 App"
            it.summary = "killProcessDelay = 300L"
            it.setOnPreferenceClickListener {
                requireActivity().closeApp(reopenApp = false, killProcessDelay = 300L)
                true
            }
        }

        Preference(requireContext()).also {
            appPreferenceGroup.addPreference(it)
            it.title = "重启 App"
            it.summary = "killProcessDelay = -1L"
            it.setOnPreferenceClickListener {
                requireActivity().closeApp(reopenApp = true, killProcessDelay = -1L)
                true
            }
        }

        Preference(requireContext()).also {
            appPreferenceGroup.addPreference(it)
            it.title = "重启 App"
            it.summary = "killProcessDelay = 300L"
            it.setOnPreferenceClickListener {
                requireActivity().closeApp(reopenApp = true, killProcessDelay = 300L)
                true
            }
        }

        //*************************************************************************************************************

        val crashPreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Crash"
        }

        Preference(requireContext()).also {
            crashPreferenceGroup.addPreference(it)
            it.title = "Crash"
            it.setOnPreferenceClickListener {
                childFragmentManager.openAlertDialogFragment(
                    title = "Crash",
                    message = "确认？应用会崩溃的！",
                    positiveText = "确定",
                    negativeText = "取消",
                    callbackBundle = bundleOf(
                        "calling_id" to "Crash"
                    ),
                    fragmentTag = "Crash"
                )
                true
            }
        }
    }

    override fun onAlertDialogPositive(alertDialog: AlertDialog, callbackBundle: Bundle): Boolean {
        return when (callbackBundle.getValue<String>("calling_id")) {
            "Crash" -> {
                throw CrashException()
            }
            else -> super.onAlertDialogPositive(alertDialog, callbackBundle)
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
