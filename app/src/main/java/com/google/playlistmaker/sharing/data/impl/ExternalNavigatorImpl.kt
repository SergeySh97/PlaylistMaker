package com.google.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.playlistmaker.R
import com.google.playlistmaker.sharing.domain.ExternalNavigator
import com.google.playlistmaker.sharing.domain.model.AgreementLinkData
import com.google.playlistmaker.sharing.domain.model.EmailData
import com.google.playlistmaker.sharing.domain.model.ShareLinkData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareAppLink() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, getShareAppLink().shareLink)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun sendSupport() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(getSupportEmail().uri)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getSupportEmail().email))
            putExtra(Intent.EXTRA_SUBJECT, getSupportEmail().subject)
            putExtra(Intent.EXTRA_TEXT, getSupportEmail().body)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openUserAgreement() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(getUserAgreement().agreementLink)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
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