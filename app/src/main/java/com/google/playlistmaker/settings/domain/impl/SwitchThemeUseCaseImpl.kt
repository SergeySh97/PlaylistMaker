package com.google.playlistmaker.settings.domain.impl

import com.google.playlistmaker.settings.domain.api.SwitchThemeRepository
import com.google.playlistmaker.settings.domain.usecase.SwitchThemeUseCase

class SwitchThemeUseCaseImpl(private val repository: SwitchThemeRepository) :
    SwitchThemeUseCase {
    override fun execute(isChecked: Boolean) {
        repository.updateTheme(isChecked)
    }

    override fun getTheme(): Boolean {
        return repository.getTheme()
    }
}