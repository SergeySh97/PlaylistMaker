package com.google.playlistmaker.media.creator.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.google.playlistmaker.media.creator.data.entity.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackInPlaylistEntity)

    @Delete
    suspend fun deleteTrack(track: TrackInPlaylistEntity)

    @Update
    suspend fun updateTrack(track: TrackInPlaylistEntity)

    @Query("SELECT * FROM track_in_playlist_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Int): TrackInPlaylistEntity?
}