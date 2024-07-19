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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.spotify.model.AlbumItem
import com.example.spotify.model.ApiInterface
import com.example.spotify.model.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private val tracks = mutableStateListOf<AlbumItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotifyApp()
        }
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
                    TrackItem(track)
                }
            }
        }
    }

    @Composable
    fun TrackItem(track: AlbumItem) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = track.data.coverArt.sources.firstOrNull()?.url).apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                        }).build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(text = track.data.name, fontSize = 18.sp)
                Text(text = track.data.artists.items.joinToString(", ") { it.profile.name }, fontSize = 14.sp)
            }
        }
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
}
