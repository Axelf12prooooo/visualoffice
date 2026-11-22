package app.visualoffice.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.visualoffice.data.settings.ThemeMode

// Palette définie dans le cahier des charges.
val Background = Color(0xFF111318)
val Panel = Color(0xFF191D24)
val PanelSecondary = Color(0xFF222833)
val Accent = Color(0xFFFF5A00)
val TextPrimary = Color(0xFFE8E6E1)
val TextSecondary = Color(0xFFA9B0BC)

private val DarkColors = darkColorScheme(
    primary = Accent,
    onPrimary = Background,
    primaryContainer = Color(0xFF4A1F00),
    onPrimaryContainer = Color(0xFFFFDBCB),
    background = Background,
    onBackground = TextPrimary,
    surface = Panel,
    onSurface = TextPrimary,
    surfaceVariant = PanelSecondary,
    onSurfaceVariant = TextSecondary,
    surfaceContainerLow = Panel,
    surfaceContainer = Panel,
    surfaceContainerHigh = PanelSecondary,
    outline = Color(0xFF3A4352),
    outlineVariant = Color(0xFF2A313C),
)

private val LightColors = lightColorScheme(
    primary = Color(0xFFD94A00),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDBCB),
    onPrimaryContainer = Color(0xFF3A1400),
    background = Color(0xFFF6F7F9),
    onBackground = Color(0xFF15181E),
    surface = Color.White,
    onSurface = Color(0xFF15181E),
    surfaceVariant = Color(0xFFE9ECF1),
    onSurfaceVariant = Color(0xFF4A5260),
    surfaceContainerLow = Color(0xFFF1F3F6),
    surfaceContainer = Color.White,
    surfaceContainerHigh = Color(0xFFE9ECF1),
    outline = Color(0xFF9AA3B2),
    outlineVariant = Color(0xFFD5DAE2),
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(6.dp),
    medium = RoundedCornerShape(10.dp),
    large = RoundedCornerShape(14.dp),
)

fun ThemeMode.resolveDark(systemDark: Boolean): Boolean = when (this) {
    ThemeMode.DARK -> true
    ThemeMode.LIGHT -> false
    ThemeMode.SYSTEM -> systemDark
}

@Composable
fun VisualOfficeTheme(mode: ThemeMode, content: @Composable () -> Unit) {
    val dark = mode.resolveDark(isSystemInDarkTheme())
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        shapes = AppShapes,
        content = content,
    )
}
