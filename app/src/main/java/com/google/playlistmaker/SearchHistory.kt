package com.google.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val prefs: SharedPreferences) {
    val editor = prefs.edit()
    fun getHistoryList(): ArrayList<Track> {
        val json = prefs.getString(SEARCH_HISTORY_LIST, null)
        val listType = object : TypeToken<ArrayList<Track>>() {}.type
        return if (json.isNullOrEmpty()) {
            ArrayList()
        } else {
            Gson().fromJson(json, listType) ?: ArrayList()
        }
    }
    fun saveHistoryList(track: Track) {
        val historyList = getHistoryList()
        if (historyList.isNotEmpty()) historyList.removeIf { it.trackId == track.trackId }
        historyList.add(track)
        if (historyList.size == 11) historyList.removeAt(0)
        val updatedList = Gson().toJson(historyList)
        editor.putString(SEARCH_HISTORY_LIST, updatedList)
        editor.apply()
    }
    fun clearHistoryList() {
        editor.remove(SEARCH_HISTORY_LIST)
        editor.apply()
    }
    companion object {
        const val SEARCH_HISTORY_LIST = "search_history_list"
    }
}