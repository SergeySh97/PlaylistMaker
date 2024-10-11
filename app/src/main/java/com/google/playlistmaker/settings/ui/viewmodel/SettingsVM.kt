package com.google.playlistmaker.settings.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.playlistmaker.app.Creator
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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val switchThemeUseCase = Creator.providerSwitchThemeUseCase()
                val sharingInteractor = Creator.providerSharingInteractor()
                SettingsVM(switchThemeUseCase, sharingInteractor)
            }
        }
    }//rm
}