package com.google.playlistmaker.domain.api

import com.google.playlistmaker.domain.models.Track

interface HistoryRepository {

    fun getHistoryList(): List<Track>

    fun saveHistoryList(expression: Track)

    fun clearhistoryList()

}