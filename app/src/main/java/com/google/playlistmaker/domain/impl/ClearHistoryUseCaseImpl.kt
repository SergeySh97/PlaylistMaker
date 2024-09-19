package com.google.playlistmaker.domain.impl

import com.google.playlistmaker.domain.api.ClearHistoryRepository
import com.google.playlistmaker.domain.usecases.ClearHistoryUseCase

class ClearHistoryUseCaseImpl(private val repository: ClearHistoryRepository) :
    ClearHistoryUseCase {
    override fun clearHistoryList() {
        return repository.clearhistoryList()
    }
}