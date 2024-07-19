package com.example.spotify.model

data class TracksResponse(
    val albums: Albums
)
data class Albums(
    val totalCount: Int,
    val items: List<AlbumItem>
)

data class AlbumItem(
    val data: AlbumData
)

data class AlbumData(
    val uri: String,
    val name: String,
    val artists: AlbumArtists,
    val coverArt: AlbumCoverArt,
    val date: AlbumDate
)

data class AlbumArtists(
    val items: List<AlbumArtist>
)

data class AlbumArtist(
    val uri: String,
    val profile: ArtistProfile
)

data class ArtistProfile(
    val name: String
)

data class AlbumCoverArt(
    val sources: List<AlbumCoverSource>
)

data class AlbumCoverSource(
    val url: String,
    val width: Int,
    val height: Int
)

data class AlbumDate(
    val year: Int
)

