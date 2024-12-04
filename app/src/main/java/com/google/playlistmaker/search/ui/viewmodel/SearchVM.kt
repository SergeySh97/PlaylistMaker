package com.google.playlistmaker.search.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.playlistmaker.search.domain.model.ErrorType
import com.google.playlistmaker.search.domain.model.Track
import com.google.playlistmaker.search.domain.usecase.ClearHistoryUseCase
import com.google.playlistmaker.search.domain.usecase.GetHistoryUseCase
import com.google.playlistmaker.search.domain.usecase.SaveHistoryUseCase
import com.google.playlistmaker.search.domain.usecase.SearchTracksUseCase
import com.google.playlistmaker.search.ui.model.SearchState
import com.google.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchVM(
    private val tracksSearchUseCase: SearchTracksUseCase,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val saveHistoryUseCase: SaveHistoryUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase
) : ViewModel() {

    private val searchState = MutableLiveData<SearchState>()

    private var searchText: String? = null ?: ""

    private val trackSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    )
    { searchTracks(it) }

    init {
        getHistory()
    }

    fun getSearchState(): LiveData<SearchState> {
        return searchState
    }

    private fun searchTracks(input: String) {
        if (input.isBlank()) {
            getHistory()
        } else {
            viewModelScope.launch {
                tracksSearchUseCase
                    .execute(input)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: ErrorType?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }
        when {
            errorMessage != null -> {
                renderState(SearchState.Error(errorMessage))
            }

            tracks.isEmpty() -> {
                renderState(SearchState.NotFound)
            }

            else -> {
                renderState(SearchState.SearchContent(tracks))
            }
        }
    }

    fun searchDebounce(input: String) {
        if (searchText != input) {
            renderState(SearchState.Loading)
            searchText = input
            trackSearchDebounce(input)
        }
    }

    fun getHistory() {
        val historyList = getHistoryUseCase.execute()
        if (historyList.isNotEmpty()) {
            renderState(SearchState.HistoryContent(historyList))
        } else {
            renderState(SearchState.EmptyHistory)
        }
    }

    fun updateHistory(): List<Track> {
        return getHistoryUseCase.execute()
    }

    fun saveHistory(track: Track) {
        saveHistoryUseCase.execute(track)
    }

    fun clearHistory() {
        clearHistoryUseCase.execute()
        renderState(SearchState.EmptyHistory)
    }

    private fun renderState(state: SearchState) {
        searchState.postValue(state)
    }

    private companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}