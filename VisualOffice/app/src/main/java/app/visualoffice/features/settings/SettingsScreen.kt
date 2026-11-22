package app.visualoffice.features.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.visualoffice.R
import app.visualoffice.core.appViewModel
import app.visualoffice.data.settings.ThemeMode

private val themeOptions = listOf(
    ThemeMode.DARK to R.string.theme_dark,
    ThemeMode.LIGHT to R.string.theme_light,
    ThemeMode.SYSTEM to R.string.theme_system,
)

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val viewModel = appViewModel { SettingsViewModel(it.settings) }
    val current by viewModel.themeMode.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val version = remember {
        runCatching { context.packageManager.getPackageInfo(context.packageName, 0).versionName }.getOrNull() ?: "?"
    }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(stringResource(R.string.settings_theme), style = MaterialTheme.typography.titleMedium)
        themeOptions.forEach { (mode, label) ->
            ThemeRow(label = label, selected = current == mode, onSelect = { viewModel.setThemeMode(mode) })
        }

        Text(
            stringResource(R.string.settings_about),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(
            "${stringResource(R.string.settings_version)} : $version",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ThemeRow(@StringRes label: Int, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().selectable(selected = selected, onClick = onSelect, role = Role.RadioButton),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(stringResource(label), modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp))
    }
}
