package com.google.playlistmaker.search.data.impl

import com.google.playlistmaker.search.data.mapper.Mapper.toTrack
import com.google.playlistmaker.search.data.mapper.Mapper.toTrackDto
import com.google.playlistmaker.search.data.sharedprefs.HistoryTracksManager
import com.google.playlistmaker.search.domain.api.HistoryRepository
import com.google.playlistmaker.search.domain.model.Track

class HistoryTracksRepositoryImpl(private val sharedPrefs: HistoryTracksManager) :
    HistoryRepository {
    override fun getHistoryList(): List<Track> {
        return sharedPrefs.getHistoryList().map { it.toTrack() }
    }

    override fun saveHistoryList(track: Track) {
        sharedPrefs.saveHistoryList(track.toTrackDto())
    }

    override fun clearHistoryList() {
        sharedPrefs.clearHistoryList()
    }


}