package com.sadili.androidapp.githubsocial.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.sadili.androidapp.githubsocial.R
import com.sadili.androidapp.githubsocial.alarm.AlarmReminder

class SettingPreference : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var REMINDER: String
    private lateinit var switchReminder: SwitchPreference
    private lateinit var alarmReminder: AlarmReminder

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference_setting)

        alarmReminder = AlarmReminder()

        initReminder()
        initSharedPreference()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {

        if (key == REMINDER) {
            switchReminder.isChecked = sharedPreferences.getBoolean(REMINDER, false)
            Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show()
        }

        val state: Boolean = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(REMINDER, false)

        setReminder(state)
    }

    private fun initReminder() {
        REMINDER = resources.getString(R.string.pref_setting)
        switchReminder = findPreference<SwitchPreference>(REMINDER) as SwitchPreference
    }

    private fun initSharedPreference() {
        val sharedPreferences = preferenceManager.sharedPreferences
        switchReminder.isChecked = sharedPreferences.getBoolean(REMINDER, false)
    }

    private fun setReminder(state: Boolean) {
        if (state) {
            context?.let {
                alarmReminder.setRepeatReminder(it)
            }
        } else {
            context?.let {
                alarmReminder.cancelAlarm(it)
            }
        }
    }
}