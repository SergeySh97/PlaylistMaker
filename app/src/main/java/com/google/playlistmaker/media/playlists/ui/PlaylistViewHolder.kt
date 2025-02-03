package com.google.playlistmaker.media.playlists.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.playlistmaker.R
import com.google.playlistmaker.media.media.domain.model.Playlist
import com.google.playlistmaker.utils.Extensions.setTrackText

class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val cover: ImageView = itemView.findViewById(R.id.iv_cover)
    private val playlistName: TextView = itemView.findViewById(R.id.tv_playlist_name)
    private val tracksCount: TextView = itemView.findViewById(R.id.tv_tracks_count)


    fun bind(playlist: Playlist) {

        playlistName.text = playlist.name
        if (playlist.tracksCount == 0) {
            tracksCount.text = null
        } else {
            tracksCount.setTrackText(playlist.tracksCount)
        }


        val radiusInDp = 8
        val density = itemView.resources.displayMetrics.density
        val radiusInPixels = (radiusInDp * density).toInt()
        Glide.with(itemView)
            .load(playlist.filePath)
            .transform(CenterCrop(), RoundedCorners(radiusInPixels))
            .placeholder(R.drawable.placeholder)
            .into(cover)
    }
}