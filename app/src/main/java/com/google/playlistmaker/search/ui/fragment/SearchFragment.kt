package com.google.playlistmaker.search.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.playlistmaker.R
import com.google.playlistmaker.databinding.FragmentSearchBinding
import com.google.playlistmaker.search.domain.model.ErrorType
import com.google.playlistmaker.search.domain.model.Track
import com.google.playlistmaker.search.ui.OnTrackClickListener
import com.google.playlistmaker.search.ui.TrackAdapter
import com.google.playlistmaker.search.ui.model.SearchState
import com.google.playlistmaker.search.ui.viewmodel.SearchVM
import com.google.playlistmaker.utils.Extensions.gone
import com.google.playlistmaker.utils.Extensions.visible
import com.google.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), OnTrackClickListener {

    private val listener: OnTrackClickListener by lazy {
        this
    }
    private val viewModel: SearchVM by viewModel()

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = requireNotNull(_binding) { "Binding wasn't initialized!" }

    private var searchText = ""

    private var onTrackClickDebounce: ((Track) -> Unit)? = null

    private var _adapter: TrackAdapter? = null
    private val adapter: TrackAdapter get() = requireNotNull(_adapter) { "Adapter wasn't initialized!" }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    override fun onTrackClick(track: Track) {
        viewModel.saveHistory(track)
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToPlayerFragment(track)
        )
    }

    private fun initializeUI() {

        viewModel.getSearchState().observe(viewLifecycleOwner) {
            renderState(it)
        }

        with(binding) {
            btClearHistory.setOnClickListener {
                viewModel.clearHistory()
            }
            ivClear.setOnClickListener {
                etSearch.text?.clear()
                viewModel.getHistory()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
            }
            etSearch.addTextChangedListener(
                onTextChanged = { s, _, _, _ ->
                    searchText = s.toString()
                    if (s?.isBlank() == true) {
                        //Делаю повторный поиск, чтобы скинуть последний отправленный запрос на сервер
                        viewModel.searchDebounce(searchText)
                        viewModel.getHistory()
                    } else {
                        viewModel.searchDebounce(searchText)
                    }
                }, afterTextChanged = { editable ->
                    ivClear.visibility = if (editable.isNullOrEmpty()) View.GONE else View.VISIBLE
                })
        }
        onTrackClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, lifecycleScope, false)
        { onTrackClick(it) }
    }

    private fun renderState(state: SearchState) {
        when (state) {
            is SearchState.SearchContent -> showSearch(state.searchList)
            SearchState.NotFound -> showError(ErrorType.NOT_FOUND)
            is SearchState.HistoryContent -> showHistory()
            SearchState.EmptyHistory -> goneHistory()
            is SearchState.Error -> showError(state.errorMessage)
            SearchState.Loading -> showLoading(true)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearch(searchList: List<Track>) {
        showLoading(false)
        with(binding) {
            val recyclerView = rvSearch
            _adapter = TrackAdapter(searchList, listener)
            llError.gone()
            recyclerView.visible()
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun showError(error: ErrorType) {
        showLoading(false)
        with(binding) {
            when (error) {
                ErrorType.NOT_FOUND -> {
                    llError.visible()
                    ivErrorSmile.visible()
                    tvError.text = getString(R.string.didnt_have)
                    rvSearch.gone()
                    ivErrorWifi.gone()
                    btRefresh.gone()
                }

                else -> {
                    llError.visible()
                    ivErrorSmile.gone()
                    ivErrorWifi.visible()
                    btRefresh.visible()
                    tvError.text = getString(R.string.internet_problem)
                    rvSearch.gone()
                    btRefresh.setOnClickListener {
                        viewModel.searchDebounce(searchText)
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory() {
        showLoading(false)
        with(binding) {
            val historyList = viewModel.updateHistory()
            _adapter = TrackAdapter(historyList, listener)
            val recyclerView = rvSearch
            llError.gone()
            tvHistory.visible()
            rvSearch.visible()
            btClearHistory.visible()
            recyclerView.visible()
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun goneHistory() {
        showLoading(false)
        with(binding) {
            tvHistory.gone()
            rvSearch.gone()
            btClearHistory.gone()
        }
    }

    private fun showLoading(isVisible: Boolean) {
        with(binding) {
            tvHistory.gone()
            llError.gone()
            rvSearch.gone()
            btClearHistory.gone()
            progressBar.isVisible = isVisible
        }
    }

    private companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}