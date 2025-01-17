package com.google.playlistmaker.favorites

import androidx.room.Database
import androidx.room.RoomDatabase
import com.google.playlistmaker.favorites.data.dao.TrackDao
import com.google.playlistmaker.favorites.data.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao

}