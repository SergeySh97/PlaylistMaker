package com.google.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                toast(R.string.search)
            }
        }
        binding.btSearch.setOnClickListener(imageClickListener)
        binding.btMedia.setOnClickListener {
            toast(R.string.media)
        }
        binding.btSettings.setOnClickListener {
            toast(R.string.settings)
        }
    }
    private fun toast(text: Int) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
    }
}