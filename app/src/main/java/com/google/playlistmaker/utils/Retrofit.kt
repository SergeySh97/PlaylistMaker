package com.google.playlistmaker.utils

import com.google.playlistmaker.ItunesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {
    fun initRetrofit(): ItunesApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ItunesApi::class.java)
    }
}