package com.google.playlistmaker.data.sharedprefs

import com.google.playlistmaker.data.dto.TrackDto

interface HistoryTracksManager {

    fun getHistoryList(): List<TrackDto>

    fun saveHistoryList(expression: TrackDto)

    fun clearHistoryList()
}