package com.google.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.playlistmaker.OnTrackClickListener
import com.google.playlistmaker.R
import com.google.playlistmaker.SearchHistory
import com.google.playlistmaker.Track
import com.google.playlistmaker.TrackAdapter
import com.google.playlistmaker.TracksFound
import com.google.playlistmaker.Utils
import com.google.playlistmaker.databinding.ActivitySearchBinding
import com.google.playlistmaker.ui.Extensions.gone
import com.google.playlistmaker.ui.Extensions.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SearchActivity : AppCompatActivity(), OnTrackClickListener {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var searchHistory: SearchHistory
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
        prefs = getSharedPreferences(PLAYLIST_PREFS, MODE_PRIVATE)
        searchHistory = SearchHistory(prefs)
        listener = this
        createHistory()
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
            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    search()
                    true
                }
                else false
            }
            etSearch.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (etSearch.hasFocus() && s?.isEmpty() == true) {
                            llHistory.visible()
                            btClearHistory.visible()
                            createHistory()
                        } else {
                            llHistory.gone()
                            btClearHistory.gone()
                        }
                }

                override fun afterTextChanged(s: Editable?) {
                    ivClear.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                    searchText = s.toString()
                }
            })
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
        searchHistory.saveHistoryList(track)
        createHistory()
        val intent = Intent(this, TrackActivity::class.java)
        intent.putExtra(TRACK, Gson().toJson(track))
        startActivity(intent)
    }
    private fun searchError(errorCode: Int) {
        binding.apply {
            rvSearch.gone()
            if (errorCode in 200..299) {
                llError.visible()
                ivErrorSmile.visible()
                tvError.text = getString(R.string.didnt_have)
                ivErrorWifi.gone()
                btRefresh.gone()
            } else {
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
    }
        private fun search() {
            if (isInternetAvailable()) {
                val etSearch = binding.etSearch.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    val response = Utils.initRetrofit().search(etSearch)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body()?.resultCount != 0) createRecycler(
                            response
                        )
                        else {
                            searchError(response.code())
                        }
                    }
                }
            }
            else {
                searchError(0)
            }
        }
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
    companion object {
        const val SEARCH_TEXT = "searchText"
        const val PLAYLIST_PREFS = "playlist_maker"
        const val TRACK = "track"
    }
}