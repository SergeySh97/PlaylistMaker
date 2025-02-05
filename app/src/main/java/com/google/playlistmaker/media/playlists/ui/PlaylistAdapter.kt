package com.google.playlistmaker.media.playlists.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.playlistmaker.R
import com.google.playlistmaker.media.media.domain.model.Playlist

class PlaylistAdapter(
    private var playlistList: List<Playlist>,
    private val listener: OnPlaylistClickListener
) : RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_adapter, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlistData = playlistList[position]
        holder.bind(playlistData)
        holder.itemView.setOnClickListener {
            listener.onPlaylistClick(playlistData)
        }
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }
}