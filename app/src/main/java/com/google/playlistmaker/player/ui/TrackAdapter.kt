package com.google.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.playlistmaker.R
import com.google.playlistmaker.search.domain.model.Track

class TrackAdapter(
    private var tracksList: List<Track>,
    private val listener: OnTrackClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_adapter, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
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
