package app.visualoffice

import android.content.Context
import androidx.room.Room
import app.visualoffice.data.db.AppDatabase
import app.visualoffice.data.db.RecentFileDao
import app.visualoffice.data.settings.SettingsRepository

/** Conteneur de dépendances manuel (suffisant à ce stade, remplaçable par Hilt/Koin plus tard). */
class AppContainer(context: Context) {
    private val database: AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "visualoffice.db").build()

    val recentFileDao: RecentFileDao get() = database.recentFileDao()
    val settings: SettingsRepository = SettingsRepository(context.applicationContext)
}
