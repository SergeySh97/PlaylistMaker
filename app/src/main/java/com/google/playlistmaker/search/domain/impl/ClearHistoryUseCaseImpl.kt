package com.google.playlistmaker.search.domain.impl

import com.google.playlistmaker.search.domain.api.HistoryRepository
import com.google.playlistmaker.search.domain.usecase.ClearHistoryUseCase

class ClearHistoryUseCaseImpl(private val repository: HistoryRepository) :
    ClearHistoryUseCase {
    override fun execute() {
        return repository.clearHistoryList()
    }
}