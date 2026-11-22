package app.visualoffice.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.visualoffice.AppContainer
import app.visualoffice.VisualOfficeApp

/** Crée un ViewModel en lui fournissant le conteneur de dépendances de l'application. */
@Composable
inline fun <reified VM : ViewModel> appViewModel(crossinline create: (AppContainer) -> VM): VM {
    val container = (LocalContext.current.applicationContext as VisualOfficeApp).container
    return viewModel(factory = viewModelFactory { initializer { create(container) } })
}
