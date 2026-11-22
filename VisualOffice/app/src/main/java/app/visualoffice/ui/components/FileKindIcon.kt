package app.visualoffice.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.Slideshow
import androidx.compose.material.icons.outlined.TableChart
import androidx.compose.ui.graphics.vector.ImageVector
import app.visualoffice.domain.FileKind

fun FileKind.icon(): ImageVector = when (this) {
    FileKind.TEXT, FileKind.MARKDOWN -> Icons.Outlined.Description
    FileKind.DOCUMENT -> Icons.Outlined.Article
    FileKind.SPREADSHEET -> Icons.Outlined.TableChart
    FileKind.PRESENTATION -> Icons.Outlined.Slideshow
    FileKind.PDF -> Icons.Outlined.PictureAsPdf
    FileKind.CODE -> Icons.Outlined.Code
    FileKind.OTHER -> Icons.Outlined.InsertDriveFile
}
