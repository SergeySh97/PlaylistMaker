package com.google.playlistmaker.search.domain.usecase

import com.google.playlistmaker.search.domain.model.Track

interface GetHistoryUseCase {

    fun execute(): List<Track>
}