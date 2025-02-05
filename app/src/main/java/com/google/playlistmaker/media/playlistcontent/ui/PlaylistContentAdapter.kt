package com.google.playlistmaker.media.playlistcontent.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.playlistmaker.R
import com.google.playlistmaker.media.media.domain.model.MediaTrack
import com.google.playlistmaker.media.media.mapper.Mapper.toTrack
import com.google.playlistmaker.search.ui.TrackViewHolder

class PlaylistContentAdapter(
    private var tracksList: List<MediaTrack>,
    private val listener: OnPlaylistTrackClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_adapter, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val trackData = tracksList[position]
        holder.bind(trackData.toTrack())
        holder.itemView.setOnClickListener {
            listener.onPlaylistTrackClick(trackData)
        }
        holder.itemView.setOnLongClickListener {
            listener.onPlaylistTrackLongClick(trackData)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return tracksList.size
    }
}