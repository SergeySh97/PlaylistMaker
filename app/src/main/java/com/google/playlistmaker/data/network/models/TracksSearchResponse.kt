package com.google.playlistmaker.data.network.models

import com.google.playlistmaker.data.dto.TrackDto


data class TracksSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()



