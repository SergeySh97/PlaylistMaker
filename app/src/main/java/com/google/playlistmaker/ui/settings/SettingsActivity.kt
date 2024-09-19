package com.google.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.playlistmaker.R
import com.google.playlistmaker.databinding.ActivitySettingsBinding
import com.google.playlistmaker.presentation.thememanager.SwitchTheme

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        initializeUI()
    }

    private fun initializeUI() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.settings) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        with(binding) {
            val switchTheme = SwitchTheme(applicationContext)
            scAppTheme.isChecked = switchTheme.isNightMode()
            scAppTheme.setOnCheckedChangeListener { _, isChecked ->
                switchTheme.switchTheme(isChecked)
            }
            btBack.setOnClickListener {
                onBackPressed()
            }
            tvShare.setOnClickListener {
                shareAppLink()
            }
            tvSupport.setOnClickListener {
                sendSupport()
            }
            tvUserAgreement.setOnClickListener {
                openUserAgreement()
            }
        }
    }

    private fun shareAppLink() {
        startActivity(Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            type = "text/plain"
        })
    }

    private fun sendSupport() {
        startActivity(Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(getString(R.string.support_uri))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body))
        })
    }

    private fun openUserAgreement() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_uri))))
    }
}