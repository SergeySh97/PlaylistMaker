package com.google.playlistmaker.media.playlists.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.playlistmaker.R
import com.google.playlistmaker.media.playlists.domain.model.Playlist

class PlaylistBottomSheetAdapter(
    private var playlistList: List<Playlist>,
    private val listener: OnPlaylistClickListener
) : RecyclerView.Adapter<PlaylistBottomSheetViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistBottomSheetViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_adapter_bottom_sheet, parent, false)
        return PlaylistBottomSheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistBottomSheetViewHolder, position: Int) {
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