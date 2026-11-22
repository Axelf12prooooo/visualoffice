package app.visualoffice.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.visualoffice.data.settings.SettingsRepository
import app.visualoffice.data.settings.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {
    val themeMode: StateFlow<ThemeMode> = repository.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.DARK)

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { repository.setThemeMode(mode) }
    }
}
