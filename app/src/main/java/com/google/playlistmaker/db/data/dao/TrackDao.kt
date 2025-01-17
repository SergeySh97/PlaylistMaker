package com.google.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.playlistmaker.db.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(track: TrackEntity)

    @Delete
    fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM track_table")
    suspend fun getTracksId(): List<Int>
}