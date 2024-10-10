package com.google.playlistmaker.sharing.domain.model

data class EmailData(
    val uri: String,
    val email: String,
    val subject: String,
    val body: String
)
