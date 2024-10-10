package com.google.playlistmaker.search.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.playlistmaker.search.domain.model.ErrorType
import com.google.playlistmaker.search.domain.model.NetworkResult
import com.google.playlistmaker.search.domain.model.Track
import com.google.playlistmaker.search.domain.usecase.ClearHistoryUseCase
import com.google.playlistmaker.search.domain.usecase.GetHistoryUseCase
import com.google.playlistmaker.search.domain.usecase.SaveHistoryUseCase
import com.google.playlistmaker.search.domain.usecase.TracksSearchUseCase
import com.google.playlistmaker.search.ui.model.SearchState

class SearchVM(
    private val tracksSearchUseCase: TracksSearchUseCase,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val saveHistoryUseCase: SaveHistoryUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase
) : ViewModel() {

    private val searchState = MutableLiveData<SearchState>()
    private val searchTextState = MutableLiveData<String>()

    init {
        getHistory()
    }

    fun getState(): LiveData<SearchState> = searchState
    fun getSearchTextState(): LiveData<String> = searchTextState

    fun searchTracks(input: String) {
        if (input.isBlank()) {
            getHistory()
        } else {
            tracksSearchUseCase.execute(input, object : TracksSearchUseCase.TracksConsumer {
                override fun consume(foundTracks: NetworkResult<List<Track>, ErrorType>) {
                    when (foundTracks) {
                        is NetworkResult.Success -> renderState(
                            SearchState.SearchContent(
                                foundTracks.data
                            )
                        )

                        is NetworkResult.Error -> searchError(foundTracks.error)
                    }
                }
            })
        }
    }

    fun searchError(error: ErrorType) {
        val state = when (error) {
            ErrorType.NOT_FOUND -> SearchState.NotFound
            else -> SearchState.Error(error)
        }
        renderState(state)
    }

    fun loading() {
        renderState(SearchState.Loading)
    }

    fun getHistory() {
        val historyList = getHistoryUseCase.execute()
        if (historyList.isNotEmpty()) {
            renderState(SearchState.HistoryContent(historyList))
        } else {
            renderState(SearchState.EmptyHistory)
        }
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
}