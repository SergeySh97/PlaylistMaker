package com.google.playlistmaker.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.playlistmaker.R
import com.google.playlistmaker.Track
import com.google.playlistmaker.databinding.ActivityTrackBinding
import com.google.playlistmaker.ui.Extensions.gone
import com.google.playlistmaker.ui.Extensions.visible
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackBinding
    private lateinit var track: Track
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)
        val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
        binding.apply {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName.trim()
            tvTrackTime.text = dateFormat.format(track.trackTimeMillis.toLong())
            tvYear.text = track.releaseDate.take(4)
            tvGenre.text = track.primaryGenreName
            tvCountry.text = track.country
            if (track.collectonName == null) {
                tvAlbum.gone()
                tvAlbumHint.gone()
            } else {
                tvAlbum.visible()
                tvAlbumHint.visible()
                tvAlbum.text = track.collectonName
            }

            val radiusInDp = 8
            val density = resources.displayMetrics.density
            val radiusInPixels = (radiusInDp * density).toInt()
            Glide.with(applicationContext)
                .load(track.getCoverArtwork())
                .transform(RoundedCorners(radiusInPixels))
                .centerCrop()
                .placeholder(R.drawable.placeholder_300dp)
                .into(ivAlbum)
            btBack.setOnClickListener { onBackPressed() }
        }
    }
    companion object {
        const val TRACK = "track"
    }
}