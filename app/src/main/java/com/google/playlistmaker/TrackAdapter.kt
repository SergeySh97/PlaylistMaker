package com.google.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackAdapter(private var trackList: ArrayList<Track>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_adapter, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val trackData = trackList[position]
        holder.bind(trackData)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
    class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView = itemView.findViewById(R.id.trackName)
        private val artistName: TextView = itemView.findViewById(R.id.artistName)
        private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
        private val artworkUrl100: ImageView = itemView.findViewById(R.id.ivAlbum)

        fun bind(trackData: Track) {
            trackName.text = trackData.trackName
            artistName.text = trackData.artistName
            trackTime.text = trackData.trackTime
            Glide.with(itemView)
                .load(trackData.artworkUrl100)
                .transform(RoundedCorners(2))
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(artworkUrl100)
        }
    }
}