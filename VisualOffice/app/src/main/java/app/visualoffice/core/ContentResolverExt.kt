package app.visualoffice.core

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns

/** Nom d'affichage d'un document SAF, avec repli sur le dernier segment de l'URI. */
fun ContentResolver.displayName(uri: Uri): String {
    val fallback = uri.lastPathSegment ?: "fichier"
    return runCatching {
        query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) cursor.getString(index) else null
            } else {
                null
            }
        }
    }.getOrNull() ?: fallback
}
