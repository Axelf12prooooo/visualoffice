package app.visualoffice.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentFileDao {
    @Query("SELECT * FROM recent_files ORDER BY openedAt DESC LIMIT :limit")
    fun observeRecent(limit: Int): Flow<List<RecentFile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(file: RecentFile)

    @Query("DELETE FROM recent_files WHERE uri = :uri")
    suspend fun delete(uri: String)
}
