package com.google.playlistmaker.data.impl

import com.google.playlistmaker.data.sharedprefs.HistoryTracksManager
import com.google.playlistmaker.data.converters.Converters.mapToData
import com.google.playlistmaker.data.converters.Converters.mapToDomain
import com.google.playlistmaker.domain.api.ClearHistoryRepository
import com.google.playlistmaker.domain.api.GetHistoryRepository
import com.google.playlistmaker.domain.api.SaveHistoryRepository
import com.google.playlistmaker.domain.models.Track

class HistoryTracksRepositoryImpl(private val sharedPrefs: HistoryTracksManager) :
    GetHistoryRepository, SaveHistoryRepository, ClearHistoryRepository {
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