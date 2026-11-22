package app.visualoffice.features.preview

import android.content.ContentResolver
import android.net.Uri
import app.visualoffice.core.displayName
import app.visualoffice.domain.FileKind

const val PREVIEW_MAX_KB = 128
private const val MAX_BYTES = PREVIEW_MAX_KB * 1024

sealed interface PreviewState {
    data object Loading : PreviewState
    data class Loaded(val name: String, val content: String, val truncated: Boolean) : PreviewState
    data class Unsupported(val name: String) : PreviewState
    data object Failed : PreviewState
}

/** Lecture bloquante : à appeler hors du thread principal. */
fun loadPreview(resolver: ContentResolver, uri: Uri): PreviewState = try {
    val name = resolver.displayName(uri)
    if (!FileKind.isTextPreviewable(name)) {
        PreviewState.Unsupported(name)
    } else {
        val stream = resolver.openInputStream(uri)
        if (stream == null) {
            PreviewState.Failed
        } else {
            stream.use { input ->
                val buffer = ByteArray(MAX_BYTES + 1)
                var total = 0
                while (total < buffer.size) {
                    val read = input.read(buffer, total, buffer.size - total)
                    if (read < 0) break
                    total += read
                }
                val truncated = total > MAX_BYTES
                val text = String(buffer, 0, minOf(total, MAX_BYTES), Charsets.UTF_8)
                PreviewState.Loaded(name, text, truncated)
            }
        }
    }
} catch (e: Exception) {
    PreviewState.Failed
}
