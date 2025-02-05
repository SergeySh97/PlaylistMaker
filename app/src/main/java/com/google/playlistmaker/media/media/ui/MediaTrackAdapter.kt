package com.google.playlistmaker.media.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.playlistmaker.R
import com.google.playlistmaker.media.media.domain.model.MediaTrack

class MediaTrackAdapter(
    private var tracksList: List<MediaTrack>,
    private val listener: OnMediaTrackClickListener
) : RecyclerView.Adapter<MediaTrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaTrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_adapter, parent, false)
        return MediaTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaTrackViewHolder, position: Int) {
        val trackData = tracksList[position]
        holder.bind(trackData)
        holder.itemView.setOnClickListener {
            listener.onTrackClick(trackData)
        }
    }

    override fun getItemCount(): Int {
        return tracksList.size
    }
}