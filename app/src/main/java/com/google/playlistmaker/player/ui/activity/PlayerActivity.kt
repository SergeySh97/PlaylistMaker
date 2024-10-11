package com.google.playlistmaker.player.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.playlistmaker.R
import com.google.playlistmaker.databinding.ActivityPlayerBinding
import com.google.playlistmaker.search.domain.model.Track
import com.google.playlistmaker.player.ui.model.PlayerState
import com.google.playlistmaker.player.ui.viewmodel.PlayerVM
import com.google.playlistmaker.utils.Extensions.gone
import com.google.playlistmaker.utils.Extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : ComponentActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private lateinit var mainThreadHandler: Handler
    //private val viewModel: PlayerVM by viewModel()
    private lateinit var viewModel: PlayerVM//rm
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        initializeUI()
        viewModel = ViewModelProvider(this,
            PlayerVM.getViewModelFactory())[PlayerVM::class.java]//rm
        viewModel.getPlayerState().observe(this) {
            renderState(it)
        }
        viewModel.getPlayingState().observe(this) {
            playbackControl(it)
        }
    }

    private fun initializeUI() {

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)
        mainThreadHandler = Handler(Looper.getMainLooper())
        with(binding) {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName.trim()
            tvDuration.text = dateFormat.format(track.trackTimeMillis.toLong())
            tvYear.text = if (track.releaseDate == NONE) track.releaseDate else track
                .releaseDate.take(4)
            tvGenre.text = track.primaryGenreName
            tvCountry.text = track.country
            @SuppressLint("SetTextI18n")
            tvTrackTime.text = DEFAULT_TIME
            if (track.collectionName == NULL) {
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

            btBack.setOnClickListener {
                @Suppress("DEPRECATION")
                onBackPressed()
            }
        }
    }

    private fun trackCurrentTime(): Runnable {
        return object : Runnable {
            override fun run() {
                if (viewModel.getPlayingState().value == true) {
                    mainThreadHandler.postDelayed(this, TRACK_TIME_DELAY)
                    val trackTime = dateFormat.format(viewModel.getCurrentPosition())
                    binding.tvTrackTime.text = trackTime
                }
            }
        }
    }

    private fun renderState(state: PlayerState) {
        when (state) {
            is PlayerState.Prepared -> preparePlayer()
            is PlayerState.Playing -> startPlayer()
            is PlayerState.Paused -> pausePlayer()
        }
    }

    private fun playbackControl(isPlaying: Boolean) {
        with(binding) {
            if (isPlaying) {
                btPlay.setOnClickListener { viewModel.pausePlayer() }
                btPlay.setImageResource(R.drawable.bt_pause)
            }
            else {
                btPlay.setOnClickListener { viewModel.startPlayer() }
                btPlay.setImageResource(R.drawable.bt_play)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun preparePlayer() {
        with(binding) {
            if (track.previewUrl != NULL) {
                    viewModel.preparePlayer(track.previewUrl,
                        {
                            onPreparedListener()
                        },{
                            onCompletionListener()
                        })
            } else {
                Toast.makeText(applicationContext, R.string.null_preview_url, Toast.LENGTH_LONG)
                    .show()
                btPlay.setOnClickListener {
                    Toast.makeText(applicationContext, R.string.null_preview_url, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun onPreparedListener() {
        binding.btPlay.isEnabled = true
    }

    private fun onCompletionListener() {
        mainThreadHandler.removeCallbacks(trackCurrentTime())
        binding.btPlay.setImageResource(R.drawable.bt_play)
        binding.tvTrackTime.text = DEFAULT_TIME
        viewModel.pausePlayer()
    }

    private fun startPlayer() {
        mainThreadHandler.post(trackCurrentTime())
        viewModel.startPlayer()
    }

    private fun pausePlayer() {
        mainThreadHandler.removeCallbacks(trackCurrentTime())
        viewModel.pausePlayer()
    }

    override fun onPause() {
        super.onPause()
        if (viewModel.getPlayingState().value == true) {
            viewModel.pausePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacks(trackCurrentTime())
        viewModel.releasePlayer()
    }

    private companion object {
        const val TRACK = "track"
        const val NULL = "null"
        const val NONE = "Неизвестно"
        const val TRACK_TIME_DELAY = 300L
        const val DEFAULT_TIME = "00:00"
    }
}