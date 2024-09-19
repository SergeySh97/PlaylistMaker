package com.google.playlistmaker.data.network

import com.google.playlistmaker.data.network.model.Response
import com.google.playlistmaker.data.network.model.TracksSearchRequest
import com.google.playlistmaker.data.network.model.StatusCode
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitNetworkClient : NetworkClient {
    private const val I_TUNES_BASE_URL = "https://itunes.apple.com"
    fun initRetrofit(): ItunesApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(I_TUNES_BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ItunesApi::class.java)
    }

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            val response = initRetrofit().searchTracks(dto.expression).execute()
            val body = response.body() ?: Response()
            return body.apply { resultCode = StatusCode(response.code()) }
        } else {
            return Response().apply { resultCode = StatusCode(400) }
        }
    }
}