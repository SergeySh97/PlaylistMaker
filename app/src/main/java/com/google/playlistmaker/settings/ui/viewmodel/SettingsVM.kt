package com.google.playlistmaker.settings.ui.viewmodel


import androidx.lifecycle.ViewModel
import com.google.playlistmaker.settings.domain.usecase.SwitchThemeUseCase
import com.google.playlistmaker.sharing.domain.usecase.SharingInteractor

class SettingsVM(
    private val switchThemeUseCase: SwitchThemeUseCase,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    fun getTheme(): Boolean {
        return switchThemeUseCase.getTheme()
    }

    fun switchTheme(isChecked: Boolean) {
        switchThemeUseCase.execute(isChecked)
    }

    fun shareAppLink() {
        sharingInteractor.shareAppLink()
    }

    fun sendSupport() {
        sharingInteractor.sendSupport()
    }

    fun openUserAgreement() {
        sharingInteractor.openUserAgreement()
    }
}