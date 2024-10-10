package com.google.playlistmaker.search.domain.impl

import com.google.playlistmaker.search.domain.api.HistoryRepository
import com.google.playlistmaker.search.domain.model.Track
import com.google.playlistmaker.search.domain.usecase.GetHistoryUseCase

class GetHistoryUseCaseImpl(private val repository: HistoryRepository) :
    GetHistoryUseCase {
    override fun execute(): List<Track> {
        return repository.getHistoryList()
    }
}