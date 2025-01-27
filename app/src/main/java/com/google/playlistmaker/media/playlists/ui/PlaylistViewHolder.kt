package com.google.playlistmaker.media.playlists.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.playlistmaker.R
import com.google.playlistmaker.media.playlists.domain.model.Playlist

class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val cover: ImageView = itemView.findViewById(R.id.iv_cover)
    private val playlistName: TextView = itemView.findViewById(R.id.tv_playlist_name)
    private val tracksCount: TextView = itemView.findViewById(R.id.tv_tracks_count)


    fun bind(playlist: Playlist) {

        playlistName.text = playlist.name
        if (playlist.tracksCount == 0) {
            tracksCount.text = null
        } else {
            tracksCount.text = getTrackText(playlist.tracksCount)
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

    private fun getTrackText(count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> "$count $TRACK_1"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "$count $TRACK_2_4"
            else -> "$count $TRACK_ELSE"
        }
    }

    private companion object {
        const val TRACK_1 = "трек"
        const val TRACK_2_4 = "трека"
        const val TRACK_ELSE = "треков"
    }
}