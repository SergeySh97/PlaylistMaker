package com.google.playlistmaker.search.ui.model

import com.google.playlistmaker.search.domain.model.ErrorType
import com.google.playlistmaker.search.domain.model.Track

sealed interface SearchState {

    data class SearchContent(val searchList: List<Track>) : SearchState
    data object NotFound : SearchState
    data class HistoryContent(val historyList: List<Track>) : SearchState
    data object EmptyHistory : SearchState
    data class Error(val errorMessage: ErrorType) : SearchState
    data object Loading : SearchState


}
