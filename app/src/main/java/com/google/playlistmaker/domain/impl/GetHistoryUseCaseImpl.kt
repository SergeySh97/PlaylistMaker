package com.google.playlistmaker.domain.impl

import com.google.playlistmaker.domain.api.GetHistoryRepository
import com.google.playlistmaker.domain.usecases.GetHistoryUseCase
import com.google.playlistmaker.domain.models.Track

class GetHistoryUseCaseImpl(private val repository: GetHistoryRepository) :
    GetHistoryUseCase {
    override fun getHistoryList(): List<Track> {
        return repository.getHistoryList()
    }
}