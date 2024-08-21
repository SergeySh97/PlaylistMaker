package com.google.playlistmaker

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.playlistmaker.TrackAdapter.TrackViewHolder

class HistoryTrackAdapter(private var trackList: ArrayList<Track>, private val sharedPreferences: SharedPreferences) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_adapter, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val trackData = trackList[position]
        holder.bind(trackData)
        holder.itemView.setOnClickListener {
            SearchHistory(sharedPreferences).saveHistoryList(trackData)
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}