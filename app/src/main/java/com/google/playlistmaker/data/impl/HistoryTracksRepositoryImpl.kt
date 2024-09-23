package com.google.playlistmaker.data.impl

import com.google.playlistmaker.data.sharedprefs.HistoryTracksManager
import com.google.playlistmaker.data.converters.Mapper.mapToData
import com.google.playlistmaker.data.converters.Mapper.mapToDomain
import com.google.playlistmaker.domain.api.HistoryRepository
import com.google.playlistmaker.domain.models.Track

class HistoryTracksRepositoryImpl(private val sharedPrefs: HistoryTracksManager) :
    HistoryRepository {
    override fun getHistoryList(): List<Track> {
        return sharedPrefs.getHistoryList().map { mapToDomain(it) }
    }

    override fun saveHistoryList(expression: Track) {
        sharedPrefs.saveHistoryList(mapToData(expression))
    }

    override fun clearhistoryList() {
        sharedPrefs.clearHistoryList()
    }


}