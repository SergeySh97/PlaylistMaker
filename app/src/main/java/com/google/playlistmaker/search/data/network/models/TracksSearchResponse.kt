package com.google.playlistmaker.search.data.network.models

import com.google.playlistmaker.search.data.dto.TrackDto


data class TracksSearchResponse(
    val results: List<TrackDto>
) : Response()



