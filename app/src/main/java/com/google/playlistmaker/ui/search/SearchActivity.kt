package com.google.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.google.gson.Gson
import com.google.playlistmaker.app.Creator
import com.google.playlistmaker.R
import com.google.playlistmaker.data.network.model.ErrorType
import com.google.playlistmaker.data.network.model.NetworkResult
import com.google.playlistmaker.data.sharedprefs.SharedPrefsManager.setupPrefs
import com.google.playlistmaker.databinding.ActivitySearchBinding
import com.google.playlistmaker.domain.models.Track
import com.google.playlistmaker.domain.usecases.TracksSearchUseCase
import com.google.playlistmaker.ui.search.ClickDebounce.clickDebounce
import com.google.playlistmaker.ui.search.SearchDebounce.searchDebounce
import com.google.playlistmaker.ui.track.OnTrackClickListener
import com.google.playlistmaker.ui.track.TrackActivity
import com.google.playlistmaker.ui.track.TrackAdapter
import com.google.playlistmaker.ui.utils.Extensions.gone
import com.google.playlistmaker.ui.utils.Extensions.visible

class SearchActivity : AppCompatActivity(), OnTrackClickListener {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchRunnable: Runnable
    private lateinit var listener: OnTrackClickListener
    private var searchText: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT)
            with(binding) {
                etSearch.setText(searchText)
                etSearch.setSelection(searchText?.length ?: 0)
            }
        }
        initializeUI()
        setupPrefs(applicationContext)
        listener = this
        createHistory()
        searchRunnable = Runnable { search() }
    }

    override fun onResume() {
        super.onResume()
        createHistory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        with(binding) {
            super.onRestoreInstanceState(savedInstanceState)
            searchText = savedInstanceState.getString(SEARCH_TEXT)
            etSearch.setText(searchText)
            etSearch.setSelection(searchText?.length ?: 0)
        }
    }

    override fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            Creator.providerSaveHistoryUseCase().saveHistoryList(track)
            createHistory()
            val intent = Intent(applicationContext, TrackActivity::class.java)
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
                onBackPressed()
            }
            btClearHistory.setOnClickListener {
                Creator.providerClearHistoryUseCase().clearHistoryList()
                llHistory.gone()
                btClearHistory.gone()
            }
            ivClear.setOnClickListener {
                etSearch.text?.clear()
                createHistory()
                llError.gone()
                rvSearch.gone()
                val imm =
                    applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
            }
            etSearch.addTextChangedListener(onTextChanged = { charSequence, _, _, _ ->
                if (etSearch.hasFocus() && charSequence?.isEmpty() == true) {
                    llHistory.visible()
                    btClearHistory.visible()
                    createHistory()
                } else {
                    llHistory.gone()
                    btClearHistory.gone()
                }
                if (charSequence?.isEmpty() == false) {
                    searchDebounce(searchRunnable)
                }
            }, afterTextChanged = { editable ->
                ivClear.visibility = if (editable.isNullOrEmpty()) View.GONE else View.VISIBLE
                searchText = editable.toString()
            })
        }
    }

    private fun createRecycler(trackList: List<Track>) {
        with(binding) {
            val recyclerView = rvSearch
            val adapter = TrackAdapter(trackList, listener)
            llError.gone()
            recyclerView.visible()
            recyclerView.adapter = adapter
            adapter?.notifyDataSetChanged()
        }
    }

    private fun createHistory() {
        with(binding) {
            val historyList = Creator.providerGetHistoryUseCase().getHistoryList()
            if (historyList.isNotEmpty()) {
                val adapter = TrackAdapter(historyList, listener)
                val recyclerView = rvHistory
                recyclerView.visible()
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            } else {
                llHistory.gone()
                btClearHistory.gone()
            }
        }
    }

    private fun search() {
        with(binding) {
            if (!isInternetAvailable()) {
                internetProblem()
            } else {
                rvSearch.gone()
                llError.gone()
                progressBar.visible()
                val etSearch = etSearch.text.toString()
                if (etSearch.isNotEmpty()) {
                    Creator.providerTracksSearchUseCase()
                        .searchTracks(etSearch, object : TracksSearchUseCase.TracksConsumer {
                            override fun consume(foundTracks: NetworkResult<List<Track>, ErrorType>) {
                                runOnUiThread {
                                    when (foundTracks) {
                                        is NetworkResult.Success -> {
                                            if (foundTracks.data.isNotEmpty()) {
                                                progressBar.gone()
                                                createRecycler(foundTracks.data)
                                            } else {
                                                notFound()
                                            }
                                        }

                                        is NetworkResult.Error -> {
                                            progressBar.gone()
                                            when (foundTracks.error) {
                                                ErrorType.NOT_FOUND -> {
                                                    notFound()
                                                }

                                                else -> {
                                                    internetProblem()
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        })
                } else {
                    progressBar.gone()
                }
            }
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    private fun notFound() {
        with(binding) {
            llError.visible()
            ivErrorSmile.visible()
            tvError.text = getString(R.string.didnt_have)
            rvSearch.gone()
            ivErrorWifi.gone()
            btRefresh.gone()
        }
    }

    private fun internetProblem() {
        with(binding) {
            llError.visible()
            ivErrorSmile.gone()
            ivErrorWifi.visible()
            btRefresh.visible()
            tvError.text = getString(R.string.internet_problem)
            rvSearch.gone()
            btRefresh.setOnClickListener {
                search()
            }
        }
    }

    private companion object {
        const val SEARCH_TEXT = "searchText"
        const val TRACK = "track"
    }
}