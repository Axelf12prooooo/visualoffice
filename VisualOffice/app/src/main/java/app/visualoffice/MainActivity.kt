package app.visualoffice

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.visualoffice.data.settings.ThemeMode
import app.visualoffice.ui.navigation.VisualOfficeRoot
import app.visualoffice.ui.theme.VisualOfficeTheme
import app.visualoffice.ui.theme.resolveDark

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as VisualOfficeApp).container
        setContent {
            val mode by container.settings.themeMode.collectAsStateWithLifecycle(initialValue = ThemeMode.DARK)
            val dark = mode.resolveDark(isSystemInDarkTheme())

            // Les icônes de barres système doivent suivre le thème de l'app, pas celui du système.
            DisposableEffect(dark) {
                val style = if (dark) {
                    SystemBarStyle.dark(Color.TRANSPARENT)
                } else {
                    SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                }
                enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
                onDispose { }
            }

            VisualOfficeTheme(mode) { VisualOfficeRoot() }
        }
    }
}
