package com.google.playlistmaker.data.network.model

import com.google.playlistmaker.data.dto.TrackDto


data class TracksSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()



