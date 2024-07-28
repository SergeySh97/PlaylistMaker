package com.google.playlistmaker

import android.content.Context
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
import com.google.playlistmaker.databinding.ActivitySearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySearchBinding
    private var searchText: String? = null
    private val utils = Utils()
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
        binding.apply {
        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT)
            etSearch.setText(searchText)
            etSearch.setSelection(searchText?.length ?: 0)
        }
            btBack.setOnClickListener {
                onBackPressed()
            }
            ivClear.setOnClickListener {
                etSearch.text?.clear()
                llError.visibility = View.GONE
                rvSearch.visibility = View.GONE
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

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    ivClear.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                    searchText = s.toString()
                }
            })
        }
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
            val adapter = trackList?.let { TrackAdapter(it) }
            binding.llError.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.adapter = adapter
            adapter?.notifyDataSetChanged()
    }
    private fun searchError(errorCode: Int) {
        binding.apply {
            rvSearch.visibility = View.GONE
            if (errorCode in 200..300) {
                llError.visibility = View.VISIBLE
                ivErrorSmile.visibility = View.VISIBLE
                tvError.text = getString(R.string.didnt_have)
                ivErrorWifi.visibility = View.GONE
                btRefresh.visibility = View.GONE
            } else {
                llError.visibility = View.VISIBLE
                ivErrorSmile.visibility = View.GONE
                ivErrorWifi.visibility = View.VISIBLE
                btRefresh.visibility = View.VISIBLE
                tvError.text = getString(R.string.internet_problem)
                btRefresh.setOnClickListener {
                    search()
                }
            }
        }
    }
        private fun search() {
        val etSearch = binding.etSearch.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val response = utils.initRetrofit().search(etSearch)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body()?.resultCount != 0) createRecycler(response)
                else searchError(response.code())
            }
        }
    }
    companion object {
        const val SEARCH_TEXT = "searchText"
    }
}