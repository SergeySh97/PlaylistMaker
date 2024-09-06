package com.google.playlistmaker.ui

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
import com.google.playlistmaker.ItunesApi
import com.google.playlistmaker.R
import com.google.playlistmaker.SearchHistory
import com.google.playlistmaker.Track
import com.google.playlistmaker.TrackAdapter
import com.google.playlistmaker.TracksFound
import com.google.playlistmaker.databinding.ActivitySearchBinding
import com.google.playlistmaker.utils.Debouncer.clickDebounce
import com.google.playlistmaker.utils.Debouncer.searchDebounce
import com.google.playlistmaker.utils.Extensions.gone
import com.google.playlistmaker.utils.Extensions.visible
import com.google.playlistmaker.utils.OnTrackClickListener
import com.google.playlistmaker.utils.Retrofit.initRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity(), OnTrackClickListener {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchHistory: SearchHistory
    private lateinit var itunesService: ItunesApi
    private lateinit var searchRunnable: Runnable
    private lateinit var listener: OnTrackClickListener
    private var searchText: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        itunesService = initRetrofit()
        val prefs = getSharedPreferences(PLAYLIST_PREFS, MODE_PRIVATE)
        searchHistory = SearchHistory(prefs)
        listener = this
        createHistory()
        searchRunnable = Runnable { search() }
        binding.apply {
            if (savedInstanceState != null) {
                searchText = savedInstanceState.getString(SEARCH_TEXT)
                etSearch.setText(searchText)
                etSearch.setSelection(searchText?.length ?: 0)
            }
            btBack.setOnClickListener {
                onBackPressed()
            }
            btClearHistory.setOnClickListener {
                searchHistory.clearHistoryList()
                llHistory.gone()
                btClearHistory.gone()
            }
            ivClear.setOnClickListener {
                etSearch.text?.clear()
                createHistory()
                llError.gone()
                rvSearch.gone()
                val imm = this@SearchActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
            }
            etSearch.addTextChangedListener(
                onTextChanged = { charSequence, _, _, _ ->
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
                },
                afterTextChanged = { editable ->
                    ivClear.visibility = if (editable.isNullOrEmpty()) View.GONE else View.VISIBLE
                    searchText = editable.toString()
                }
            )
        }
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
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT)
        binding.etSearch.setText(searchText)
        binding.etSearch.setSelection(searchText?.length ?: 0)
    }

    private fun createRecycler(response: Response<TracksFound>) {
        val trackList = response.body()?.results
        val recyclerView = binding.rvSearch
        val adapter = trackList?.let { TrackAdapter(it, listener) }
        binding.llError.gone()
        recyclerView.visible()
        recyclerView.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    private fun createHistory() {
        val historyList = searchHistory.getHistoryList()
        if (historyList.isNotEmpty()) {
            val adapter = TrackAdapter(historyList, listener)
            val recyclerView = binding.rvHistory
            recyclerView.visible()
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        } else {
            binding.llHistory.gone()
            binding.btClearHistory.gone()
        }
    }

    override fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            searchHistory.saveHistoryList(track)
            createHistory()
            val intent = Intent(this, TrackActivity::class.java)
            intent.putExtra(TRACK, Gson().toJson(track))
            startActivity(intent)
        }
    }

    private fun search() {
        if (!isInternetAvailable()) {
            searchError(0)
        } else {
            binding.rvSearch.gone()
            binding.llError.gone()
            binding.progressBar.visible()
            val etSearch = binding.etSearch.text.toString()
            if (etSearch.isNotEmpty()) {
                itunesService.search(etSearch).enqueue(object : Callback<TracksFound> {
                    override fun onResponse(
                        call: Call<TracksFound>,
                        response: Response<TracksFound>
                    ) {
                        binding.rvSearch.visible()
                        binding.progressBar.gone()
                        if (response.isSuccessful && response.body()?.resultCount != 0) {
                            createRecycler(response)
                        } else {
                            searchError(response.code())
                        }
                    }

                    override fun onFailure(p0: Call<TracksFound>, p1: Throwable) {
                        binding.rvSearch.visible()
                        binding.progressBar.gone()
                        searchError(0)
                    }
                })
            } else {
                binding.progressBar.gone()
            }
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    private fun searchError(errorCode: Int) {
        binding.rvSearch.gone()
        when (errorCode) {
            in 200..299 -> {
                notFound()
            }

            404 -> {
                notFound()
            }

            else -> {
                internetProblem()
            }
        }
    }

    private fun notFound() {
        binding.apply {
            llError.visible()
            ivErrorSmile.visible()
            tvError.text = getString(R.string.didnt_have)
            ivErrorWifi.gone()
            btRefresh.gone()
        }
    }

    private fun internetProblem() {
        binding.apply {
            llError.visible()
            ivErrorSmile.gone()
            ivErrorWifi.visible()
            btRefresh.visible()
            tvError.text = getString(R.string.internet_problem)
            btRefresh.setOnClickListener {
                search()
            }
        }
    }

    private companion object {
        const val SEARCH_TEXT = "searchText"
        const val PLAYLIST_PREFS = "playlist_maker"
        const val TRACK = "track"
    }
}