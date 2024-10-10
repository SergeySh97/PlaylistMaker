package com.google.playlistmaker.search.data.sharedprefs

import com.google.playlistmaker.search.data.dto.TrackDto

interface HistoryTracksManager {

    fun getHistoryList(): List<TrackDto>

    fun saveHistoryList(expression: TrackDto)

    fun clearHistoryList()
}