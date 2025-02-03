package com.google.playlistmaker.media.creator.data.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.playlistmaker.media.creator.AppDatabase
import com.google.playlistmaker.media.creator.data.entity.TrackInPlaylistEntity
import com.google.playlistmaker.media.creator.domain.api.CreatorRepository
import com.google.playlistmaker.media.media.domain.model.MediaTrack
import com.google.playlistmaker.media.media.mapper.Mapper.toPlaylist
import com.google.playlistmaker.media.media.mapper.Mapper.toPlaylistEntity
import com.google.playlistmaker.media.media.mapper.Mapper.toTrackInPlaylistEntity
import com.google.playlistmaker.media.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CreatorRepositoryImpl(
    private val appDatabase: AppDatabase
) : CreatorRepository {
    override suspend fun createPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlist.toPlaylistEntity())
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(playlist.toPlaylistEntity())
        jsonToTrackList(playlist.trackList).iterator().forEach {
            checkTrackInPlaylists(it)
        }
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists()
            .map { playlistEntity -> playlistEntity.map { it.toPlaylist() } }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlist.toPlaylistEntity())
    }

    override suspend fun addTrackIntoPlaylist(track: MediaTrack, playlist: Playlist) {
        appDatabase.trackInPlaylistDao()
            .insertTrack(track.toTrackInPlaylistEntity())
        appDatabase.playlistDao()
            .updatePlaylist(addTrackToTrackList(playlist, track).toPlaylistEntity())
    }

    override suspend fun deleteTrackFromPlaylist(track: MediaTrack, playlist: Playlist): Flow<Playlist> {
        checkTrackInPlaylists(track)
        appDatabase.playlistDao()
            .updatePlaylist(removeTrackFromTrackList(playlist, track).toPlaylistEntity())
        return appDatabase.playlistDao().getPlaylistById(playlist.playlistId).map { it.toPlaylist() }
    }


    private fun addTrackToTrackList(playlist: Playlist, track: MediaTrack): Playlist =
        playlist.copy(
            trackList = addTrackToJson(playlist.trackList, track),
            tracksCount = playlist.tracksCount + 1
        )

    private fun addTrackToJson(json: String, newTrack: MediaTrack): String {
        val tracks = jsonToTrackList(json)
        val updatedTracks = tracks.toMutableList().apply { add(newTrack) }
        return Gson().toJson(updatedTracks)
    }

    private fun removeTrackFromTrackList(playlist: Playlist, track: MediaTrack): Playlist =
        playlist.copy(
            trackList = removeTrackFromJson(playlist.trackList, track),
            tracksCount = playlist.tracksCount - 1
        )

    private fun removeTrackFromJson(json: String, track: MediaTrack): String {
        val tracks = jsonToTrackList(json)
        val updatedTracks = tracks.toMutableList().apply { remove(track) }
        return Gson().toJson(updatedTracks)
    }

    private fun jsonToTrackList(json: String): List<MediaTrack> {
        val listType = object : TypeToken<List<MediaTrack>>() {}.type
        return Gson().fromJson(json, listType) ?: emptyList()
    }

    private suspend fun checkTrackInPlaylists(track: MediaTrack) {
        var playlistCount = 0
        val playlistsFlow = appDatabase.playlistDao().getPlaylists()
        playlistsFlow.map { playlists ->
             playlists.iterator().forEach { playlist ->
                 jsonEntityToTrackList(playlist.trackList).iterator().forEach nested@ {
                     if (it.trackId == track.trackId) {
                         playlistCount++
                         return@nested
                     }
                 }
             }
        }
        if (playlistCount < 2) {
            appDatabase.trackInPlaylistDao().deleteTrack(track.toTrackInPlaylistEntity())
        }
    }

    private fun jsonEntityToTrackList(json: String): List<TrackInPlaylistEntity> {
        val listType = object : TypeToken<List<TrackInPlaylistEntity>>() {}.type
        return Gson().fromJson(json, listType) ?: emptyList()
    }
}