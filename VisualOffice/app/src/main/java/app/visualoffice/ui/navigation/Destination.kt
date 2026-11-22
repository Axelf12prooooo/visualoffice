package app.visualoffice.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.FolderSpecial
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import app.visualoffice.R

enum class Destination(val route: String, @StringRes val label: Int, val icon: ImageVector) {
    Home("home", R.string.nav_home, Icons.Outlined.Home),
    Documents("documents", R.string.nav_documents, Icons.Outlined.Description),
    Code("code", R.string.nav_code, Icons.Outlined.Code),
    Projects("projects", R.string.nav_projects, Icons.Outlined.FolderSpecial),
    Files("files", R.string.nav_files, Icons.Outlined.Folder),
    Settings("settings", R.string.nav_settings, Icons.Outlined.Settings),
}

const val PREVIEW_ROUTE = "preview"
