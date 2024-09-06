package com.google.playlistmaker.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.google.playlistmaker.utils.Extensions.gone
import com.google.playlistmaker.utils.Extensions.visible
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackBinding
    private lateinit var track: Track
    private lateinit var mainThreadHandler: Handler
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var url: String = ""
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
        mainThreadHandler = Handler(Looper.getMainLooper())
        binding.apply {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName.trim()
            tvDuration.text = dateFormat.format(track.trackTimeMillis.toLong())
            tvYear.text = track.releaseDate.take(4)
            tvGenre.text = track.primaryGenreName
            tvCountry.text = track.country
            tvTrackTime.text = "00:00"
            url = track.previewUrl
            if (track.collectionName == null) {
                tvAlbum.gone()
                tvAlbumHint.gone()
            } else {
                tvAlbum.visible()
                tvAlbumHint.visible()
                tvAlbum.text = track.collectionName
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
            btPlay.setOnClickListener { playbackControl() }
        }
        preparePlayer()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.btPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.btPlay.setImageResource(R.drawable.bt_play)
            playerState = STATE_PREPARED
            mainThreadHandler.removeCallbacks(trackCurrentTime())
            binding.tvTrackTime.text = "00:00"
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.btPlay.setImageResource(R.drawable.bt_pause)
        playerState = STATE_PLAYING
        mainThreadHandler.post(trackCurrentTime())
    }

    private fun pausePlayer() {
        mainThreadHandler.removeCallbacks(trackCurrentTime())
        mediaPlayer.pause()
        binding.btPlay.setImageResource(R.drawable.bt_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun trackCurrentTime(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING && mediaPlayer.isPlaying) {
                    val trackTime = dateFormat.format(mediaPlayer.currentPosition.toLong())
                    binding.tvTrackTime.text = trackTime
                    mainThreadHandler.postDelayed(this, TRACK_TIME_DELAY)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (playerState == STATE_PAUSED) {
            startPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacks(trackCurrentTime())
        mediaPlayer.release()
    }

    private companion object {
        const val TRACK = "track"
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val TRACK_TIME_DELAY = 300L
    }
}