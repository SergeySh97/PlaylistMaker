package com.google.playlistmaker.media.creator

import androidx.room.Database
import androidx.room.RoomDatabase
import com.google.playlistmaker.media.creator.data.dao.PlaylistDao
import com.google.playlistmaker.media.creator.data.dao.TrackInPlaylistDao
import com.google.playlistmaker.media.creator.data.entity.PlaylistEntity
import com.google.playlistmaker.media.creator.data.entity.TrackInPlaylistEntity
import com.google.playlistmaker.media.favorites.data.dao.TrackDao
import com.google.playlistmaker.media.favorites.data.entity.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class, PlaylistEntity::class, TrackInPlaylistEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao

    abstract fun trackInPlaylistDao(): TrackInPlaylistDao

    abstract fun trackDao(): TrackDao
}