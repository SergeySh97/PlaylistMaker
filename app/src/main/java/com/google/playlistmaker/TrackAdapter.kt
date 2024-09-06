package com.google.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.playlistmaker.utils.OnTrackClickListener
import java.text.SimpleDateFormat
import java.util.Locale

class TrackAdapter(
    private var trackList: List<Track>,
    private val listener: OnTrackClickListener
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_adapter, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val trackData = trackList[position]
        holder.bind(trackData)
        holder.itemView.setOnClickListener {
            listener.onTrackClick(trackData)
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

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
                .transform(RoundedCorners(radiusInPixels))
                .centerCrop()
                .placeholder(R.drawable.placeholder_35dp)
                .into(artworkUrl100)
            artistName.requestLayout()
        }
    }
}
