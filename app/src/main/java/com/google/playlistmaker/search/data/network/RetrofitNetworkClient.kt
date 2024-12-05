package com.google.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.playlistmaker.search.data.network.models.Response
import com.google.playlistmaker.search.data.network.models.StatusCode
import com.google.playlistmaker.search.data.network.models.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val itunesApi: ItunesApi,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected(context)) {
            return Response().apply { resultCode = StatusCode(-1) }
        }

        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = StatusCode(400) }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = itunesApi.searchTracks(dto.expression)
                response.apply { resultCode = StatusCode(200) }
            } catch (e: Throwable) {
                Response().apply { resultCode = StatusCode(501) }
            }
        }
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }
}