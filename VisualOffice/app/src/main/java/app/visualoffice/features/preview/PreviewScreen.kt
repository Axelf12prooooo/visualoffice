package app.visualoffice.features.preview

import android.net.Uri
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.visualoffice.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PreviewScreen(uriString: String?, onBack: () -> Unit, modifier: Modifier = Modifier) {
    val resolver = LocalContext.current.contentResolver
    val state by produceState<PreviewState>(PreviewState.Loading, uriString) {
        value = if (uriString == null) {
            PreviewState.Failed
        } else {
            withContext(Dispatchers.IO) { loadPreview(resolver, Uri.parse(uriString)) }
        }
    }

    Column(modifier.fillMaxSize().padding(horizontal = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = stringResource(R.string.preview_back))
            }
            val title = when (val s = state) {
                is PreviewState.Loaded -> s.name
                is PreviewState.Unsupported -> s.name
                else -> ""
            }
            Text(title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }

        when (val s = state) {
            PreviewState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            PreviewState.Failed -> Message(stringResource(R.string.preview_error))
            is PreviewState.Unsupported -> Message(stringResource(R.string.preview_unsupported))
            is PreviewState.Loaded -> Column(Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
                if (s.truncated) {
                    Text(
                        stringResource(R.string.preview_truncated, PREVIEW_MAX_KB),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
                SelectionContainer {
                    Text(
                        s.content,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .horizontalScroll(rememberScrollState()),
                    )
                }
            }
        }
    }
}

@Composable
private fun Message(text: String) {
    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
