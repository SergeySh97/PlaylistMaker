package com.google.playlistmaker.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.playlistmaker.R
import com.google.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.settings) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        switchTheme()
        binding.apply {
            btBack.setOnClickListener {
                onBackPressed()
            }
            tvShare.setOnClickListener {
                startActivity(Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
                    type = "text/plain"
                })
            }
            tvSupport.setOnClickListener {
                startActivity(Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse(getString(R.string.support_uri))
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body))
                })
            }
            tvUserAgreement.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.user_agreement_uri))
                    )
                )
            }
        }
    }

    private fun switchTheme() {
        val prefs = getSharedPreferences(PLAYLIST_MAKER, MODE_PRIVATE)
        val appTheme = prefs.getBoolean(APP_THEME, true)
        binding.scAppTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            prefs.edit().putBoolean(APP_THEME, isChecked).apply()
        }
        if (appTheme) binding.scAppTheme.isChecked = true
    }

    companion object {
        const val PLAYLIST_MAKER = "playlist_maker"
        const val APP_THEME = "app_theme"
    }
}