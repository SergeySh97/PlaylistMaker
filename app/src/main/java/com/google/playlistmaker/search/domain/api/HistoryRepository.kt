package com.google.playlistmaker.search.domain.api

import com.google.playlistmaker.search.domain.model.Track

interface HistoryRepository {

    fun getHistoryList(): List<Track>

    fun saveHistoryList(track: Track)

    fun clearHistoryList()

}