package app.visualoffice.features.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.visualoffice.R
import app.visualoffice.core.appViewModel
import app.visualoffice.data.db.RecentFile
import app.visualoffice.domain.FileKind
import app.visualoffice.ui.components.icon
import app.visualoffice.ui.navigation.Destination
import java.text.DateFormat
import java.util.Date

private data class QuickAccess(val destination: Destination, @StringRes val description: Int)

private val quickAccessItems = listOf(
    QuickAccess(Destination.Documents, R.string.quick_documents_desc),
    QuickAccess(Destination.Code, R.string.quick_code_desc),
    QuickAccess(Destination.Projects, R.string.quick_projects_desc),
    QuickAccess(Destination.Files, R.string.quick_files_desc),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onNavigate: (Destination) -> Unit,
    onOpenPreview: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = appViewModel { HomeViewModel(it.recentFileDao) }
    val recent by viewModel.recent.collectAsStateWithLifecycle()
    val resolver = LocalContext.current.contentResolver

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            viewModel.onFileOpened(resolver, uri)
            onOpenPreview(uri.toString())
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(170.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Image(painterResource(R.drawable.ic_logo), contentDescription = null, modifier = Modifier.size(56.dp))
                Column {
                    Text(stringResource(R.string.home_welcome), style = MaterialTheme.typography.headlineSmall)
                    Text(
                        stringResource(R.string.home_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { picker.launch(arrayOf("*/*")) }) {
                        Icon(Icons.Outlined.FolderOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text(stringResource(R.string.action_open_file), modifier = Modifier.padding(start = 8.dp))
                    }
                    // Désactivés tant que VisualWriter / la gestion de projets n'existent pas.
                    OutlinedButton(onClick = {}, enabled = false) { Text(stringResource(R.string.action_new_document)) }
                    OutlinedButton(onClick = {}, enabled = false) { Text(stringResource(R.string.action_new_project)) }
                }
                Text(
                    stringResource(R.string.home_creation_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(stringResource(R.string.home_quick_access), style = MaterialTheme.typography.titleMedium)
        }
        items(quickAccessItems) { item ->
            OutlinedCard(
                onClick = { onNavigate(item.destination) },
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(item.destination.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(stringResource(item.destination.label), style = MaterialTheme.typography.titleSmall)
                    Text(
                        stringResource(item.description),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(stringResource(R.string.home_recent_files), style = MaterialTheme.typography.titleMedium)
        }
        if (recent.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    stringResource(R.string.home_recent_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            items(recent, key = { it.uri }, span = { GridItemSpan(maxLineSpan) }) { file ->
                RecentRow(
                    file = file,
                    onClick = { onOpenPreview(file.uri) },
                    onRemove = { viewModel.remove(file.uri) },
                )
            }
        }
    }
}

@Composable
private fun RecentRow(file: RecentFile, onClick: () -> Unit, onRemove: () -> Unit) {
    val date = remember(file.openedAt) {
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(file.openedAt))
    }
    ListItem(
        headlineContent = { Text(file.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        supportingContent = { Text(date) },
        leadingContent = { Icon(FileKind.fromName(file.name).icon(), contentDescription = null) },
        trailingContent = {
            IconButton(onClick = onRemove) {
                Icon(Icons.Outlined.Close, contentDescription = stringResource(R.string.remove_recent))
            }
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.clip(MaterialTheme.shapes.medium).clickable(onClick = onClick),
    )
}
