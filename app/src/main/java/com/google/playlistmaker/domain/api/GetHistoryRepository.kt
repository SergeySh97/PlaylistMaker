package com.google.playlistmaker.domain.api

import com.google.playlistmaker.domain.models.Track

interface GetHistoryRepository {

    fun getHistoryList(): List<Track>
}