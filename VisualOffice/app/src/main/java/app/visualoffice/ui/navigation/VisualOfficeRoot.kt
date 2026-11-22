package app.visualoffice.ui.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.visualoffice.R
import app.visualoffice.features.home.HomeScreen
import app.visualoffice.features.preview.PreviewScreen
import app.visualoffice.features.settings.SettingsScreen
import app.visualoffice.ui.components.ModulePlaceholder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualOfficeRoot() {
    val nav = rememberNavController()
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    var previewUri by rememberSaveable { mutableStateOf<String?>(null) }

    // Smartphone : 5 entrées en barre basse ; les Paramètres sont accessibles via l'engrenage.
    val mainDestinations = Destination.entries.filter { it != Destination.Settings }

    fun go(destination: Destination) {
        nav.navigate(destination.route) {
            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    val title = when (currentRoute) {
        null, Destination.Home.route -> stringResource(R.string.app_name)
        PREVIEW_ROUTE -> stringResource(R.string.preview_title)
        else -> Destination.entries.firstOrNull { it.route == currentRoute }
            ?.let { stringResource(it.label) } ?: stringResource(R.string.app_name)
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val wide = maxWidth >= 600.dp // tablette : barre latérale

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = { Text(title) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    actions = {
                        IconButton(onClick = { go(Destination.Settings) }) {
                            Icon(Destination.Settings.icon, contentDescription = stringResource(R.string.nav_settings))
                        }
                    },
                )
            },
            bottomBar = {
                if (!wide) {
                    NavigationBar {
                        mainDestinations.forEach { d ->
                            NavigationBarItem(
                                selected = currentRoute == d.route,
                                onClick = { go(d) },
                                icon = { Icon(d.icon, contentDescription = null) },
                                label = { Text(stringResource(d.label)) },
                            )
                        }
                    }
                }
            },
        ) { padding ->
            Row(Modifier.padding(padding)) {
                if (wide) {
                    NavigationRail {
                        mainDestinations.forEach { d ->
                            NavigationRailItem(
                                selected = currentRoute == d.route,
                                onClick = { go(d) },
                                icon = { Icon(d.icon, contentDescription = null) },
                                label = { Text(stringResource(d.label)) },
                            )
                        }
                    }
                }
                NavHost(
                    navController = nav,
                    startDestination = Destination.Home.route,
                    modifier = Modifier.weight(1f),
                ) {
                    composable(Destination.Home.route) {
                        HomeScreen(
                            onNavigate = { go(it) },
                            onOpenPreview = { uri ->
                                previewUri = uri
                                nav.navigate(PREVIEW_ROUTE) { launchSingleTop = true }
                            },
                        )
                    }
                    composable(Destination.Documents.route) {
                        ModulePlaceholder(Destination.Documents.icon, stringResource(R.string.placeholder_documents))
                    }
                    composable(Destination.Code.route) {
                        ModulePlaceholder(Destination.Code.icon, stringResource(R.string.placeholder_code))
                    }
                    composable(Destination.Projects.route) {
                        ModulePlaceholder(Destination.Projects.icon, stringResource(R.string.placeholder_projects))
                    }
                    composable(Destination.Files.route) {
                        ModulePlaceholder(Destination.Files.icon, stringResource(R.string.placeholder_files))
                    }
                    composable(Destination.Settings.route) { SettingsScreen() }
                    composable(PREVIEW_ROUTE) {
                        PreviewScreen(uriString = previewUri, onBack = { nav.popBackStack() })
                    }
                }
            }
        }
    }
}
