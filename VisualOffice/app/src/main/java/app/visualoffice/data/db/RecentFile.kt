package app.visualoffice.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_files")
data class RecentFile(
    @PrimaryKey val uri: String,
    val name: String,
    val mimeType: String?,
    val openedAt: Long,
)
