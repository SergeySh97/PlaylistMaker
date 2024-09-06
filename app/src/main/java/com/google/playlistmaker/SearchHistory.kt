package com.google.playlistmaker

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val prefs: SharedPreferences) {
    fun getHistoryList(): List<Track> {
        val json = prefs.getString(SEARCH_HISTORY_LIST, null)
        val listType = object : TypeToken<List<Track>>() {}.type
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            Gson().fromJson(json, listType) ?: emptyList()
        }
    }

    fun saveHistoryList(track: Track) {
        val historyList = getHistoryList().toMutableList()
        if (historyList.isNotEmpty()) {
            historyList.removeIf { it.trackId == track.trackId }
        }
        historyList.add(0, track)
        if (historyList.size == 11) {
            historyList.removeAt(10)
        }
        val updatedList = Gson().toJson(historyList)
        prefs.edit {
            putString(SEARCH_HISTORY_LIST, updatedList)
        }
    }

    fun clearHistoryList() {
        prefs.edit { remove(SEARCH_HISTORY_LIST) }
    }

    companion object {
        const val SEARCH_HISTORY_LIST = "search_history_list"
    }
}