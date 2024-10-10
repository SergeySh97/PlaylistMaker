package com.google.playlistmaker.settings.domain.usecase

interface SwitchThemeUseCase {

    fun execute(isChecked: Boolean)

    fun getTheme(): Boolean
}