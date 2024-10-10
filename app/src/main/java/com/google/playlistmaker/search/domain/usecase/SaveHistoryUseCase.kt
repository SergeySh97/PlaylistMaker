package com.google.playlistmaker.search.domain.usecase

import com.google.playlistmaker.search.domain.model.Track

interface SaveHistoryUseCase {

    fun execute(track: Track)

}