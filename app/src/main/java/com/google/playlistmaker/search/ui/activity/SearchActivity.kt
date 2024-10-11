package com.google.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.playlistmaker.R
import com.google.playlistmaker.databinding.ActivitySearchBinding
import com.google.playlistmaker.search.domain.model.ErrorType
import com.google.playlistmaker.search.domain.model.Track
import com.google.playlistmaker.search.ui.ClickDebounce.clickDebounce
import com.google.playlistmaker.search.ui.SearchDebounce.searchDebounce
import com.google.playlistmaker.search.ui.model.SearchState
import com.google.playlistmaker.search.ui.viewmodel.SearchVM
import com.google.playlistmaker.player.ui.OnTrackClickListener
import com.google.playlistmaker.player.ui.TrackAdapter
import com.google.playlistmaker.player.ui.activity.PlayerActivity
import com.google.playlistmaker.utils.Extensions.gone
import com.google.playlistmaker.utils.Extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), OnTrackClickListener {

    private val listener: OnTrackClickListener by lazy {
        this
    }
    //private val viewModel: SearchVM by viewModel()
    private lateinit var viewModel: SearchVM//rm

    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    private val searchRunnable: Runnable by lazy {
        Runnable { searchText.let { viewModel.searchTracks(it) } }
    }

    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        initializeUI()
        viewModel = ViewModelProvider(
            this,
            SearchVM.getViewModelFactory())[SearchVM::class.java]//rm

        viewModel.getState().observe(this) {
            renderState(it)
        }
    }

    override fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            viewModel.saveHistory(track)
            val intent = Intent(applicationContext, PlayerActivity::class.java)
            intent.putExtra(TRACK, Gson().toJson(track))
            startActivity(intent)
        }
    }

    private fun initializeUI() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        with(binding) {
            btBack.setOnClickListener {
                @Suppress("DEPRECATION") onBackPressed()
            }
            btClearHistory.setOnClickListener {
                viewModel.clearHistory()
            }
            ivClear.setOnClickListener {
                etSearch.text?.clear()
                viewModel.getHistory()
                llError.gone()
                rvSearch.gone()
                val imm =
                    applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
            }
            etSearch.addTextChangedListener(onTextChanged = { s, _, _, _ ->
                searchText = s.toString()
                if (etSearch.hasFocus() && s?.isEmpty() == true) {
                    viewModel.getHistory()
                }
                if (s?.isEmpty() == false) {
                    viewModel.loading()
                    searchDebounce(searchRunnable)
                }
            }, afterTextChanged = { editable ->
                ivClear.visibility = if (editable.isNullOrEmpty()) View.GONE else View.VISIBLE
            })
        }
    }

    private fun renderState(state: SearchState) {
        when (state) {
            is SearchState.SearchContent -> showSearch(state.searchList)
            SearchState.NotFound -> showError(ErrorType.NOT_FOUND)
            is SearchState.HistoryContent -> showHistory(state.historyList)
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
            val adapter = TrackAdapter(searchList, listener)
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
                        viewModel.loading()
                        searchDebounce(searchRunnable)
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory(historyList: List<Track>) {
        showLoading(false)
        with(binding) {
            val adapter = TrackAdapter(historyList, listener)
            val recyclerView = rvHistory
            llError.gone()
            rvSearch.gone()
            llHistory.visible()
            recyclerView.visible()
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun goneHistory() {
        showLoading(false)
        with(binding) {
            llHistory.gone()
        }
    }

    private fun showLoading(isVisible: Boolean) {
        with(binding) {
            llHistory.gone()
            llError.gone()
            rvSearch.gone()
            progressBar.isVisible = isVisible
        }
    }

    private companion object {
        const val TRACK = "track"
    }
}