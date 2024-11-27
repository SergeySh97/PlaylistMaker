package com.google.playlistmaker.sharing.domain.impl

import com.google.playlistmaker.sharing.domain.ExternalNavigator
import com.google.playlistmaker.sharing.domain.usecase.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun shareAppLink() {
        externalNavigator.shareAppLink()
    }

    override fun sendSupport() {
        externalNavigator.sendSupport()
    }

    override fun openUserAgreement() {
        externalNavigator.openUserAgreement()
    }


}