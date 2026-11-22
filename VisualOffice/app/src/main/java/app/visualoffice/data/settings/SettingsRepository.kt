package app.visualoffice.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode { DARK, LIGHT, SYSTEM }

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {
    private val themeKey = stringPreferencesKey("theme_mode")

    /** Thème sombre par défaut, conformément à l'identité visuelle de l'application. */
    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        prefs[themeKey]?.let { stored -> runCatching { ThemeMode.valueOf(stored) }.getOrNull() } ?: ThemeMode.DARK
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[themeKey] = mode.name }
    }
}
