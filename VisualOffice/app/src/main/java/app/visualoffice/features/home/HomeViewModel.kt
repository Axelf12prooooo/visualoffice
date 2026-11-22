package app.visualoffice.features.home

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.visualoffice.core.displayName
import app.visualoffice.data.db.RecentFile
import app.visualoffice.data.db.RecentFileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val dao: RecentFileDao) : ViewModel() {

    val recent: StateFlow<List<RecentFile>> = dao.observeRecent(20)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onFileOpened(resolver: ContentResolver, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            // Conserve l'accès au fichier après redémarrage (Storage Access Framework).
            runCatching { resolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION) }
            dao.upsert(
                RecentFile(
                    uri = uri.toString(),
                    name = resolver.displayName(uri),
                    mimeType = resolver.getType(uri),
                    openedAt = System.currentTimeMillis(),
                ),
            )
        }
    }

    fun remove(uri: String) {
        viewModelScope.launch { dao.delete(uri) }
    }
}
