package com.google.playlistmaker.domain.api

import com.google.playlistmaker.domain.models.Track

interface SaveHistoryRepository {

    fun saveHistoryList(expression: Track)

}