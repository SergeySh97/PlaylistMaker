package com.google.playlistmaker.domain.usecases

import com.google.playlistmaker.domain.models.Track

interface GetHistoryUseCase {

    fun getHistoryList(): List<Track>
}