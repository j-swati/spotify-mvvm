package com.example.spotify.model

data class TrackDetailsResponse(
    val tracks: List<TrackDetails>
)

data class TrackDetails(
    val album: Album,
    val artists: List<ArtistX>,
    val discNumber: Int,
    val durationMs: Int,
    val explicit: Boolean,
    val externalIds: ExternalIds,
    val externalUrls: ExternalUrlsXXX,
    val id: String,
    val isLocal: Boolean,
    val isPlayable: Boolean,
    val name: String,
    val popularity: Int,
    val previewUrl: String,
    val trackNumber: Int,
    val type: String,
    val uri: String
)

data class Album(
    val albumType: String,
    val artists: List<ArtistX>,
    val externalUrls: ExternalUrlsXXX,
    val id: String,
    val images: List<Image>,
    val name: String,
    val releaseDate: String,
    val releaseDatePrecision: String,
    val totalTracks: Int,
    val type: String,
    val uri: String
)

data class ArtistX(
    val externalUrls: ExternalUrlsXXX,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

data class ExternalIds(
    val isrc: String
)

data class ExternalUrlsXXX(
    val spotify: String
)

data class Image(
    val height: Int,
    val url: String,
    val width: Int
)
