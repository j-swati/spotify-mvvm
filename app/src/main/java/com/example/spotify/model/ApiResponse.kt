package com.example.spotify.model


data class TracksResponse(
    val albums: Albums,
    val songs:AlbumTracks
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
data class AlbumTracks(
    val items: List<ItemXX>,
    val totalCount: Int
)
data class ItemXX(
    val songdata: DataXX
)
data class DataXX(
    val albumOfTracks: AlbumOfTracksX,
    val artists: AlbumArtists,
    val contentRating: ContentRating,
    val duration:Duration,
    val id:String,
    val name:String,
    val playability: Playability,
    val uri: String
)
data class AlbumOfTracksX(
    val coverArt: AlbumCoverArt,
    val id: String,
    val name: String,
    val uri:String
)
data class ContentRating(
        val label: String
        )
data class Duration(
    val totalMillisecond: Int
)
data class Playability(
    val playable: Boolean
)
// Track details response
data class TrackDetailsResponse(
    val tracks: List<Track>
)

data class Track(
    val album: Album,
    val artists: List<ArtistX>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_ids: ExternalIds,
    val external_urls: ExternalUrlsXXX,
    val id: String,
    val is_local: Boolean,
    val is_playable: Boolean,
    val name: String,
    val popularity: Int,
    val preview_url: String,
    val track_number: Int,
    val type: String,
    val uri: String
)

data class Album(
    val album_type: String,
    val artists: List<ArtistX>,
    val external_urls: ExternalUrlsXXX,
    val id: String,
    val images: List<Image>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val total_tracks: Int,
    val type: String,
    val uri: String
)

data class ArtistX(
    val external_urls: ExternalUrlsXXX,
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