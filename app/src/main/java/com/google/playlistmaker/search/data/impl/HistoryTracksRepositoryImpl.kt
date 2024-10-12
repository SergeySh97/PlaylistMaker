package com.google.playlistmaker.search.data.impl

import com.google.playlistmaker.search.data.mapper.Mapper.toTrack
import com.google.playlistmaker.search.data.mapper.Mapper.toTrackDto
import com.google.playlistmaker.search.data.sharedprefs.HistoryTracksManager
import com.google.playlistmaker.search.domain.api.HistoryRepository
import com.google.playlistmaker.search.domain.model.Track

class HistoryTracksRepositoryImpl(private val manager: HistoryTracksManager) :
    HistoryRepository {
    override fun getHistoryList(): List<Track> {
        return manager.getHistoryList().map { it.toTrack() }
    }

    override fun saveHistoryList(track: Track) {
        manager.saveHistoryList(track.toTrackDto())
    }

    override fun clearHistoryList() {
        manager.clearHistoryList()
    }


}