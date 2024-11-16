package com.google.playlistmaker.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.playlistmaker.R
import com.google.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.tv_track_name)
    private val artistName: TextView = itemView.findViewById(R.id.tv_artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.tv_track_time)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.iv_album)
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    fun bind(trackData: Track) {

        trackName.text = trackData.trackName
        artistName.text = trackData.artistName.trim()
        trackTime.text = dateFormat.format(trackData.trackTimeMillis.toLong())

        val radiusInDp = 2
        val density = itemView.resources.displayMetrics.density
        val radiusInPixels = (radiusInDp * density).toInt()
        Glide.with(itemView)
            .load(trackData.artworkUrl100)
            .transform(CenterCrop(), RoundedCorners(radiusInPixels))
            .placeholder(R.drawable.placeholder)
            .into(artworkUrl100)
        artistName.requestLayout()
    }
}