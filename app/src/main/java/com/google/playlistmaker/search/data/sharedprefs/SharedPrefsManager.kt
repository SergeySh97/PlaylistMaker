package com.google.playlistmaker.search.data.sharedprefs

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.playlistmaker.search.data.dto.TrackDto


class SharedPrefsManager(context: Context) : HistoryTracksManager {
    private var prefs = context.getSharedPreferences(PLAYLIST_MAKER, Context.MODE_PRIVATE)

    override fun getHistoryList(): List<TrackDto> {
        val json = prefs?.getString(SEARCH_HISTORY_LIST, null)
        val listType = object : TypeToken<List<TrackDto>>() {}.type
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            Gson().fromJson(json, listType) ?: emptyList()
        }
    }

    override fun saveHistoryList(expression: TrackDto) {
        val historyList = getHistoryList().toMutableList()
        historyList.removeAll { it.trackId == expression.trackId }
        historyList.add(0, expression)
        if (historyList.size > 10) {
            historyList.removeLast()
        }
        val updatedList = Gson().toJson(historyList)
        prefs?.edit { putString(SEARCH_HISTORY_LIST, updatedList) }
    }

    override fun clearHistoryList() {
        prefs?.edit { remove(SEARCH_HISTORY_LIST) }
    }

    private companion object {
        const val SEARCH_HISTORY_LIST = "search_history_list"
        const val PLAYLIST_MAKER = "playlist_maker_prefs"
    }
}