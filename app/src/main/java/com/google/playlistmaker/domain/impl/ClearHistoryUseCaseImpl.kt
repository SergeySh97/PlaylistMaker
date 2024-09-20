package com.google.playlistmaker.domain.impl

import com.google.playlistmaker.domain.api.HistoryRepository
import com.google.playlistmaker.domain.usecases.ClearHistoryUseCase

class ClearHistoryUseCaseImpl(private val repository: HistoryRepository) :
    ClearHistoryUseCase {
    override fun clearHistoryList() {
        return repository.clearhistoryList()
    }
}