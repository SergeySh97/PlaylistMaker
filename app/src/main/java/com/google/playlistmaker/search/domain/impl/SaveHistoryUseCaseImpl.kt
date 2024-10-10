package com.google.playlistmaker.search.domain.impl

import com.google.playlistmaker.search.domain.api.HistoryRepository
import com.google.playlistmaker.search.domain.model.Track
import com.google.playlistmaker.search.domain.usecase.SaveHistoryUseCase

class SaveHistoryUseCaseImpl(private val repository: HistoryRepository) :
    SaveHistoryUseCase {
    override fun execute(track: Track) {
        return repository.saveHistoryList(track)
    }
}