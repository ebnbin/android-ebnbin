package com.ebnbin.windowcamera.profile.fragment

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutManager
import android.os.Bundle
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.graphics.drawable.IconCompat
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.applicationId
import com.ebnbin.eb.preference.Preference
import com.ebnbin.eb.preference.PreferenceCategory
import com.ebnbin.eb.requireSystemService
import com.ebnbin.eb.sdk25N1
import com.ebnbin.eb.sdk26O
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.main.MainActivity
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.profile.enumeration.Profile
import com.ebnbin.windowcamera.util.IOHelper

class OtherProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
//        PreferenceCategory(requireContext()).apply {
//            buildPreference(this, ProfileHelper.personalization)
//            setTitle(R.string.profile_personalization_title)
//        }
//
//        PreferenceCategory(requireContext()).apply {
//            buildPreference(this, ProfileHelper.file)
//            setTitle(R.string.profile_file_title)
//        }
//
        Preference(requireContext()).apply {
            buildPreference(this, ProfileHelper.path)
            setTitle(R.string.profile_path_title)
            summary = IOHelper.getPath().toString()
            setIcon(R.drawable.profile_path)
        }

        Preference(requireContext()).apply {
            preferenceScreen.addPreference(this)
            val helpId = when (Profile.get(ProfileHelper.profile.value)) {
                Profile.DEFAULT -> R.string.profile_default_help
                Profile.WALKING -> R.string.profile_walking_help
                Profile.MIRROR -> R.string.profile_mirror_help
                Profile.CUSTOM_1 -> R.string.profile_custom_1_help
                Profile.CUSTOM_2 -> R.string.profile_custom_2_help
            }
            setSummary(helpId)
            setIcon(R.drawable.profile_help)
        }

        PreferenceCategory(requireContext()).apply {
            buildPreference(this, ProfileHelper.quick)
            setTitle(R.string.profile_quick_title)
        }

        Preference(requireContext()).apply {
            buildPreference(this, ProfileHelper.shortcut, ProfileHelper.quick)
            setTitle(R.string.profile_shortcut_title)
            setOnPreferenceClickListener {
                if (sdk25N1()) {
                    val shortcutInfo = ShortcutInfoCompat.Builder(requireContext(), "shortcut_profile_1")
                        .setShortLabel(Profile.get().title)
                        .setIcon(IconCompat.createWithResource(requireContext(), Profile.get().iconId))
                        .setIntent(Intent(requireContext(), MainActivity::class.java)
                            .setAction("${EBApplication.instance.applicationId}.shortcut_profile_1"))
                        .build()
                        .toShortcutInfo()
                    EBApplication.instance.requireSystemService<ShortcutManager>().addDynamicShortcuts(listOf(shortcutInfo))
                }
                false
            }
        }

        Preference(requireContext()).apply {
            buildPreference(this, ProfileHelper.pinned_shortcut, ProfileHelper.quick)
            setTitle(R.string.profile_pinned_shortcut_title)
            setOnPreferenceClickListener {
                if (sdk26O()) {
                    if (EBApplication.instance.requireSystemService<ShortcutManager>().isRequestPinShortcutSupported) {
                        val shortcutInfo = ShortcutInfoCompat.Builder(requireContext(), "pinned_shortcut_profile_1")
                            .setShortLabel(Profile.get().title)
                            .setIcon(IconCompat.createWithResource(requireContext(), Profile.get().iconId))
                            .setIntent(Intent(requireContext(), MainActivity::class.java)
                                .setAction("${EBApplication.instance.applicationId}.pinned_shortcut_profile_1"))
                            .build()
                            .toShortcutInfo()
                        val intent = EBApplication.instance.requireSystemService<ShortcutManager>().createShortcutResultIntent(shortcutInfo)
                        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
                        EBApplication.instance.requireSystemService<ShortcutManager>().requestPinShortcut(shortcutInfo, pendingIntent.intentSender)
                    }
                }
                false
            }
        }
    }
}
