package com.google.playlistmaker.domain.impl

import com.google.playlistmaker.domain.api.SwitchThemeRepository
import com.google.playlistmaker.domain.usecases.SwitchThemeUseCase

class SwitchThemeUseCaseImpl(private val repository: SwitchThemeRepository):
SwitchThemeUseCase {
    override fun switchTheme(isNightMode: Boolean) {
        repository.switchTheme(isNightMode)
    }

    override fun isNightMode(): Boolean {
        return repository.isNightMode()
    }
}