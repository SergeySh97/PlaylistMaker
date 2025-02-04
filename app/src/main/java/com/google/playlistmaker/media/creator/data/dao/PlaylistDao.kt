package com.google.playlistmaker.media.creator.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.google.playlistmaker.media.creator.data.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("DELETE from playlist_table WHERE playlistId IN (:playlistId)")
    suspend fun deletePlaylistById(playlistId: Int)

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    fun getPlaylistById(playlistId: Int): Flow<PlaylistEntity>

    @Update
    fun updatePlaylist(playlist: PlaylistEntity)
}