package com.google.playlistmaker.sharing.domain.impl

import android.content.Context
import com.google.playlistmaker.R
import com.google.playlistmaker.sharing.domain.ExternalNavigator
import com.google.playlistmaker.sharing.domain.model.AgreementLinkData
import com.google.playlistmaker.sharing.domain.model.EmailData
import com.google.playlistmaker.sharing.domain.model.ShareLinkData
import com.google.playlistmaker.sharing.domain.usecase.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) : SharingInteractor {
    override fun shareAppLink() {
        externalNavigator.shareAppLink(getShareAppLink())
    }

    override fun sendSupport() {
        externalNavigator.sendSupport(getSupportEmail())
    }

    override fun openUserAgreement() {
        externalNavigator.openUserAgreement(getUserAgreement())
    }

    private fun getShareAppLink(): ShareLinkData {
        return ShareLinkData(
            shareLink = context.getString(SHARE_LINK)
        )
    }

    private fun getSupportEmail(): EmailData {
        return EmailData(
            uri = context.getString(SUPPORT_URI),
            email = context.getString(SUPPORT_EMAIL),
            subject = context.getString(SUPPORT_SUBJECT),
            body = context.getString(SUPPORT_BODY)
        )
    }

    private fun getUserAgreement(): AgreementLinkData {
        return AgreementLinkData(
            agreementLink = context.getString(AGREEMENT_LINK)
        )
    }

    private companion object {
        val SHARE_LINK = R.string.share_link
        val SUPPORT_URI = R.string.support_uri
        val SUPPORT_EMAIL = R.string.support_email
        val SUPPORT_SUBJECT = R.string.support_subject
        val SUPPORT_BODY = R.string.support_body
        val AGREEMENT_LINK = R.string.user_agreement_uri
    }
}