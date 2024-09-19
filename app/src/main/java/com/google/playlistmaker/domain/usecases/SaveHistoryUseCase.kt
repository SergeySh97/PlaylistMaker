package com.google.playlistmaker.domain.usecases

import com.google.playlistmaker.domain.models.Track

interface SaveHistoryUseCase {

    fun saveHistoryList(track: Track)

}