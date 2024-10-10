package com.google.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.playlistmaker.sharing.domain.ExternalNavigator
import com.google.playlistmaker.sharing.domain.model.AgreementLinkData
import com.google.playlistmaker.sharing.domain.model.EmailData
import com.google.playlistmaker.sharing.domain.model.ShareLinkData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareAppLink(shareLink: ShareLinkData) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, shareLink.shareLink)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun sendSupport(supportEmail: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(supportEmail.uri)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmail.email))
            putExtra(Intent.EXTRA_SUBJECT, supportEmail.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmail.body)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openUserAgreement(agreementLink: AgreementLinkData) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(agreementLink.agreementLink)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}