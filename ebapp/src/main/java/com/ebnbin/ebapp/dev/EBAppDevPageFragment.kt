package com.ebnbin.ebapp.dev

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.coroutine.Loading
import com.ebnbin.eb.getNightModeToString
import com.ebnbin.eb.preference.DropDownPreference
import com.ebnbin.eb.preference.Preference
import com.ebnbin.eb.preference.PreferenceCategory
import com.ebnbin.eb.toast
import com.ebnbin.ebapp.EBAppSpManager
import kotlinx.coroutines.Job

internal class EBAppDevPageFragment : PreferenceFragmentCompat() {
    private val viewModel: EBAppDevPageViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = EBAppSpManager.name
        preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //*************************************************************************************************************

        val nightModePreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Night Mode"
        }

        DropDownPreference(requireContext()).also {
            it.key = EBAppSpManager.night_mode.key
            it.setDefaultValue(EBAppSpManager.night_mode.defaultValue)
            nightModePreferenceGroup.addPreference(it)
            it.title = "Night Mode"
            val nightModes = arrayOf(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
                AppCompatDelegate.MODE_NIGHT_NO,
                AppCompatDelegate.MODE_NIGHT_YES,
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY,
                AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
            )
            it.entryValues = nightModes
                .map { nightMode -> nightMode.toString() }
                .toTypedArray()
            it.entries = nightModes
                .map { nightMode -> getNightModeToString(nightMode) }
                .toTypedArray()
            it.dialogTitle = it.title
        }

        //*************************************************************************************************************

        val apiPreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Api"
        }

        Preference(requireContext()).also {
            apiPreferenceGroup.addPreference(it)
            it.title = "Api"
            viewModel.ebappJson.observe(viewLifecycleOwner, Observer { value ->
                it.summary = value
            })
            viewModel.ebappJson.addLoading(viewLifecycleOwner.lifecycle, object : Loading<String?> {
                override fun onStart(job: Job) {
                    super.onStart(job)
                    requireContext().toast("onStart $job")
                }

                override fun onSuccess(result: String?) {
                    super.onSuccess(result)
                    requireContext().toast("onSuccess $result")
                }

                override fun onFailure(throwable: Throwable) {
                    super.onFailure(throwable)
                    requireContext().toast("onFailure $throwable")
                }
            })
            it.setOnPreferenceClickListener { _ ->
                it.summary = null
                viewModel.ebappJson.coroutineSetValue()
                true
            }
        }
    }
}
