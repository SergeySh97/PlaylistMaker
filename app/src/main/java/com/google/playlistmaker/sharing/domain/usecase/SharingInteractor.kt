package com.google.playlistmaker.sharing.domain.usecase

interface SharingInteractor {
    fun shareAppLink()
    fun sendSupport()
    fun openUserAgreement()
}