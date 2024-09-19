package com.google.playlistmaker.domain.impl

import com.google.playlistmaker.domain.api.SaveHistoryRepository
import com.google.playlistmaker.domain.usecases.SaveHistoryUseCase
import com.google.playlistmaker.domain.models.Track

class SaveHistoryUseCaseImpl(private val repository: SaveHistoryRepository) :
    SaveHistoryUseCase {
    override fun saveHistoryList(track: Track) {
        return repository.saveHistoryList(track)
    }
}