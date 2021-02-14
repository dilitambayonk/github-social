package com.sadili.androidapp.githubsocial.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sadili.androidapp.githubsocial.R

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.apply {
            title = getString(R.string.title_setting)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, SettingPreference()).commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}