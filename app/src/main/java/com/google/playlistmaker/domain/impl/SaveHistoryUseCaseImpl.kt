package com.google.playlistmaker.domain.impl

import com.google.playlistmaker.domain.api.HistoryRepository
import com.google.playlistmaker.domain.usecases.SaveHistoryUseCase
import com.google.playlistmaker.domain.models.Track

class SaveHistoryUseCaseImpl(private val repository: HistoryRepository) :
    SaveHistoryUseCase {
    override fun saveHistoryList(track: Track) {
        return repository.saveHistoryList(track)
    }
}