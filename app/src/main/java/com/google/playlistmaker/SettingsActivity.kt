package com.google.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        binding.btBack.setOnClickListener {
            onBackPressed()
        }
        binding.tvShare.setOnClickListener {
            startActivity(Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, SHARE_LINK)
                type = "text/plain"
            })
        }
        binding.tvSupport.setOnClickListener {
            startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse(SUPPORT_URI)))
        }
        binding.tvUserAgreement.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(USER_AGREEMENT_URI)))
        }
    }
    companion object {
        const val SUPPORT_URI = "mailto:worldoftanks14@yandex.ru" +
                "?subject=Сообщение разработчикам и разработчицам приложения Playlist Maker" +
                "&body=Спасибо разработчикам и разработчицам за крутое приложение!"
        const val USER_AGREEMENT_URI = "https://yandex.ru/legal/practicum_offer/"
        const val SHARE_LINK = "https://practicum.yandex.ru/profile/android-developer/"
    }
}