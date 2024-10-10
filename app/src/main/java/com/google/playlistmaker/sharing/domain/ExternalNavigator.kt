package com.google.playlistmaker.sharing.domain

import com.google.playlistmaker.sharing.domain.model.AgreementLinkData
import com.google.playlistmaker.sharing.domain.model.EmailData
import com.google.playlistmaker.sharing.domain.model.ShareLinkData


interface ExternalNavigator {
    fun shareAppLink(shareLink: ShareLinkData)
    fun sendSupport(supportEmail: EmailData)
    fun openUserAgreement(agreementLink: AgreementLinkData)
}