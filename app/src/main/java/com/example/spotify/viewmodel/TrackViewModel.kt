package com.example.spotify.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.spotify.api.RetrofitClient
import com.example.spotify.model.Track
import com.example.spotify.model.TrackDetails
import com.example.spotify.repository.TrackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TrackViewModel : ViewModel() {
    private val repository = TrackRepository(RetrofitClient.apiService)
    private var player: ExoPlayer? = null

    private val _tracks = MutableStateFlow<List<Track>?>(null)
    val tracks: StateFlow<List<Track>?> = _tracks

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _trackDetails = MutableStateFlow<TrackDetails?>(null)
    val trackDetails: StateFlow<TrackDetails?> = _trackDetails

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    fun searchTracks(query: String) {
        _query.value = query
        _loading.value = true
        repository.searchTracks(query) { tracks ->
            _loading.value = false
            if (tracks != null) {
                _tracks.value = tracks
                _error.value = null
            } else {
                _error.value = "Failed to load tracks"
            }
        }
    }

    /*fun getTrackDetails(trackId: String) {
        repository.getTrackDetails(trackId) { trackDetails ->
            if (trackDetails != null) {
                _trackDetails.value = trackDetails
                _error.value = null
            } else {
                _error.value = "Failed to load track details"
            }
        }
    }*/

    fun getTrackDetails(trackId: String) {
        repository.getTrackDetails(trackId) { trackDetails ->
            if (trackDetails != null) {
                _trackDetails.value = trackDetails
                _error.value = null
                // Debugging: Print or log track details
                println("Debug: Track details received: $trackDetails")
            } else {
                _error.value = "Failed to load track details"
            }
        }
    }


    fun initializePlayer(context: Context) {
        player = ExoPlayer.Builder(context).build()
    }

    fun togglePlayPause() {
        player?.let { exoPlayer ->
            val trackUrl = _trackDetails.value?.previewUrl
            if (trackUrl.isNullOrEmpty()) {
                _error.value = "Preview URL is not available or empty"
                // Optionally log or print the value of trackUrl for debugging
                println("Debug: Preview URL is $trackUrl")
                return
            }

            if (isPlaying.value) {
                exoPlayer.pause()
                _isPlaying.value = false
            } else {
                exoPlayer.setMediaItem(MediaItem.fromUri(trackUrl))
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
                _isPlaying.value = true
            }
        } ?: run {
            _error.value = "Player not initialized"
        }
    }


    override fun onCleared() {
        super.onCleared()
        player?.release()
    }
}
