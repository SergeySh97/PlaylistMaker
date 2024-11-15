package com.google.playlistmaker.player.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.playlistmaker.R
import com.google.playlistmaker.databinding.ActivityPlayerBinding
import com.google.playlistmaker.player.ui.model.PlayerState
import com.google.playlistmaker.player.ui.viewmodel.PlayerVM
import com.google.playlistmaker.search.domain.model.Track
import com.google.playlistmaker.utils.Extensions.gone
import com.google.playlistmaker.utils.Extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private val binding: ActivityPlayerBinding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }
    private var track: Track? = null
    private var mainThreadHandler: Handler? = null
    private val viewModel: PlayerVM by viewModel()
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeUI()
        viewModel.getPlayerState().observe(this) {
            renderState(it)
        }
        viewModel.getPlayingState().observe(this) {
            playbackControl(it)
        }
    }

    private fun initializeUI() {

        track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)
        mainThreadHandler = Handler(Looper.getMainLooper())
        track?.let { t ->
            with(binding) {
                tvTrackName.text = t.trackName
                tvArtistName.text = t.artistName.trim()
                tvDuration.text = dateFormat.format(t.trackTimeMillis.toLong())
                tvYear.text = if (t.releaseDate == NONE) t.releaseDate else t
                    .releaseDate.take(4)
                tvGenre.text = t.primaryGenreName
                tvCountry.text = t.country
                @SuppressLint("SetTextI18n")
                tvTrackTime.text = DEFAULT_TIME
                if (t.collectionName == NULL) {
                    tvAlbum.gone()
                    tvAlbumHint.gone()
                } else {
                    tvAlbum.visible()
                    tvAlbumHint.visible()
                    tvAlbum.text = t.collectionName
                }
                val radiusInDp = 8
                val density = resources.displayMetrics.density
                val radiusInPixels = (radiusInDp * density).toInt()
                Glide.with(applicationContext)
                    .load(t.getCoverArtwork())
                    .transform(CenterCrop(), RoundedCorners(radiusInPixels))
                    .placeholder(R.drawable.placeholder)
                    .into(ivAlbum)

                btBack.setOnClickListener {
                    @Suppress("DEPRECATION")
                    onBackPressed()
                }
            }
        }
    }

    private fun trackCurrentTime(): Runnable {
        return object : Runnable {
            override fun run() {
                if (viewModel.getPlayingState().value == true) {
                    mainThreadHandler!!.postDelayed(this, TRACK_TIME_DELAY)
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
            btPlay.setImageResource(
                if (isPlaying) {
                    R.drawable.bt_pause
                } else {
                    R.drawable.bt_play
                }
            )
            btPlay.setOnClickListener {
                if (isPlaying) {
                    viewModel.pausePlayer()
                } else {
                    viewModel.startPlayer()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun preparePlayer() {
        with(binding) {
            if (track!!.previewUrl != NULL) {
                viewModel.preparePlayer(track!!.previewUrl,
                    {
                        onPreparedListener()
                    }, {
                        onCompletionListener()
                    })
            } else {
                showToast()
                btPlay.setOnClickListener {
                    showToast()
                }
            }
        }
    }

    private fun showToast() {
        Toast.makeText(applicationContext, R.string.null_preview_url, Toast.LENGTH_LONG)
            .show()
    }

    private fun onPreparedListener() {
        binding.btPlay.isEnabled = true
    }

    private fun onCompletionListener() {
        with(binding) {
            mainThreadHandler!!.removeCallbacks(trackCurrentTime())
            btPlay.setImageResource(R.drawable.bt_play)
            tvTrackTime.text = DEFAULT_TIME
            viewModel.pausePlayer()
        }
    }

    private fun startPlayer() {
        mainThreadHandler!!.post(trackCurrentTime())
        viewModel.startPlayer()
    }

    private fun pausePlayer() {
        mainThreadHandler!!.removeCallbacks(trackCurrentTime())
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
        mainThreadHandler!!.removeCallbacks(trackCurrentTime())
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