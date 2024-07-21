package com.example.spotify

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.spotify.model.AlbumItem
import com.example.spotify.model.ApiInterface
import com.example.spotify.model.Track
import com.example.spotify.model.TrackDetailsResponse
import com.example.spotify.model.TracksResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private val tracks = mutableStateListOf<AlbumItem>()
    private val trackDetails = mutableStateListOf<Track>()
    private var exoPlayer: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotifyApp()
        }
        fetchInitialTracks()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
    }

    @Composable
    fun SpotifyApp() {
        var query by remember { mutableStateOf("") }
        val context = LocalContext.current

        val retrofit = Retrofit.Builder()
            .baseUrl("https://spotify23.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(ApiInterface::class.java)

        Column(modifier = Modifier.padding(16.dp)) {
            BasicTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .border(BorderStroke(1.dp, Color.Black)),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    if (query.isNotEmpty()) {
                        searchTracks(apiInterface, query)
                    } else {
                        Toast.makeText(context, "Please enter a search query", Toast.LENGTH_SHORT).show()
                    }
                })
            )
            Button(
                onClick = {
                    if (query.isNotEmpty()) {
                        searchTracks(apiInterface, query)
                    } else {
                        Toast.makeText(context, "Please enter a search query", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(tracks) { track ->
                    TrackItem(track, apiInterface)
                }
                items(trackDetails) { trackDetail ->
                    TrackDetailItem(trackDetail)
                }
            }
        }
    }

    @Composable
    fun TrackItem(track: AlbumItem, apiInterface: ApiInterface) {
        val scope = rememberCoroutineScope()
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = track.data.coverArt.sources.firstOrNull()?.url).apply {
                                crossfade(true)
                            }.build()
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = track.data.name, fontSize = 18.sp)
                    Text(text = track.data.artists.items.joinToString(", ") { it.profile.name }, fontSize = 14.sp)
                }
                Button(onClick = {
                    scope.launch {
                        getTrackDetails(apiInterface, track.data.uri.split(":").last())
                    }
                }) {
                    Text("Details")
                }
            }
        }
    }

    @Composable
    fun TrackDetailItem(track: Track) {
        LocalContext.current

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = track.album.images.firstOrNull()?.url).apply {
                                crossfade(true)
                            }.build()
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = track.name, fontSize = 18.sp)
                    Text(text = track.artists.joinToString(", ") { it.name }, fontSize = 14.sp)
                    Text(text = "Album: ${track.album.name}", fontSize = 14.sp)
                }
                Button(onClick = {
                    playPreview(track.preview_url)
                }) {
                    Text("Play Preview")
                }
            }
        }
    }

    private fun fetchInitialTracks() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://spotify23.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(ApiInterface::class.java)

        searchTracks(apiInterface, "q") // Replace "initial query" with a default search term if needed
    }

    private fun searchTracks(apiInterface: ApiInterface, query: String) {
        val call = apiInterface.search(query)

        call.enqueue(object : Callback<TracksResponse> {
            override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                if (response.isSuccessful) {
                    val albums = response.body()?.albums?.items
                    if (albums != null) {
                        tracks.clear()
                        tracks.addAll(albums)
                    } else {
                        Toast.makeText(this@MainActivity, "No results found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.errorBody()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getTrackDetails(apiInterface: ApiInterface, trackId: String) {
        val call = apiInterface.getTracks(trackId)

        call.enqueue(object : Callback<TrackDetailsResponse> {
            override fun onResponse(call: Call<TrackDetailsResponse>, response: Response<TrackDetailsResponse>) {
                if (response.isSuccessful) {
                    val trackDetailsList = response.body()?.tracks
                    if (trackDetailsList != null) {
                        trackDetails.clear()
                        trackDetails.addAll(trackDetailsList)
                    } else {
                        Toast.makeText(this@MainActivity, "No details found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.errorBody()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TrackDetailsResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun playPreview(previewUrl: String?) {
        if (previewUrl.isNullOrEmpty()) {
            Toast.makeText(this, "Preview URL is not available", Toast.LENGTH_SHORT).show()
            return
        }

        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(this).build()
        }

        exoPlayer?.apply {
            setMediaItem(MediaItem.fromUri(previewUrl))
            prepare()
            play()
        }
    }
}
