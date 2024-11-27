package com.google.playlistmaker.sharing.domain

interface ExternalNavigator {
    fun shareAppLink()
    fun sendSupport()
    fun openUserAgreement()
}