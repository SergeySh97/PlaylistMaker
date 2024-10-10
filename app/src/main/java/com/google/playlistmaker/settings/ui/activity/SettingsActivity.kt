package com.google.playlistmaker.settings.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.playlistmaker.databinding.ActivitySettingsBinding
import com.google.playlistmaker.settings.ui.viewmodel.SettingsVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsVM by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUI()
    }

    private fun initializeUI() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.settings) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        with(binding) {
            scAppTheme.isChecked = viewModel.getTheme()
            scAppTheme.setOnCheckedChangeListener { _, isChecked ->
                viewModel.switchTheme(isChecked)
            }
            btBack.setOnClickListener {
                @Suppress("DEPRECATION")
                onBackPressed()
            }
            tvShare.setOnClickListener {
                viewModel.shareAppLink()
            }
            tvSupport.setOnClickListener {
                viewModel.sendSupport()
            }
            tvUserAgreement.setOnClickListener {
                viewModel.openUserAgreement()
            }
        }
    }
}