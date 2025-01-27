package com.google.playlistmaker.media.creator.data.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.playlistmaker.media.creator.AppDatabase
import com.google.playlistmaker.media.creator.domain.api.CreatorRepository
import com.google.playlistmaker.media.domain.model.MediaTrack
import com.google.playlistmaker.media.mapper.Mapper.toPlaylist
import com.google.playlistmaker.media.mapper.Mapper.toPlaylistEntity
import com.google.playlistmaker.media.mapper.Mapper.toTrackInPlaylistEntity
import com.google.playlistmaker.media.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CreatorRepositoryImpl(
    private val appDatabase: AppDatabase,
) : CreatorRepository {
    override suspend fun createPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlist.toPlaylistEntity())
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(playlist.toPlaylistEntity())
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists()
            .map { it.map { playlistEntity -> playlistEntity.toPlaylist() } }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlist.toPlaylistEntity())
    }

    override suspend fun addTrackIntoPlaylist(track: MediaTrack, playlist: Playlist) {
        appDatabase.trackInPlaylistDao()
            .insertTrack(track.toTrackInPlaylistEntity())
        appDatabase.playlistDao()
            .updatePlaylist(updateTrackList(playlist, track).toPlaylistEntity())
    }

    private fun updateTrackList(playlist: Playlist, track: MediaTrack): Playlist =
        playlist.copy(
            trackList = addTrackToJson(playlist.trackList, track),
            tracksCount = playlist.tracksCount + 1
        )

    private fun addTrackToJson(json: String, newTrack: MediaTrack): String {
        val gson = Gson()
        val type = object : TypeToken<List<MediaTrack>>() {}.type
        val tracks = gson.fromJson<List<MediaTrack>>(json, type) ?: emptyList()
        val updatedTracks = tracks.toMutableList().apply { add(newTrack) }
        return gson.toJson(updatedTracks)
    }
}