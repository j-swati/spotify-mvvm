package com.example.spotify.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.spotify.viewmodel.TrackViewModel
import com.example.spotify.model.Track
@Composable
fun HomeScreen(navController: NavController, viewModel: TrackViewModel) {
    val context = LocalContext.current
    val tracksState by viewModel.tracks.collectAsState()
    val queryState by viewModel.query.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val errorState by viewModel.error.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = queryState,
            onValueChange = { viewModel.searchTracks(it) },
            placeholder = { Text("Search for tracks") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .border(BorderStroke(1.dp, Color.Gray)),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                if (queryState.isNotEmpty()) {
                    viewModel.searchTracks(queryState)
                } else {
                    Toast.makeText(context, "Please enter a search query", Toast.LENGTH_SHORT).show()
                }
            })
        )
        Button(
            onClick = {
                if (queryState.isNotEmpty()) {
                    viewModel.searchTracks(queryState)
                } else {
                    Toast.makeText(context, "Please enter a search query", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }
        if (isLoading) {
            Text("Loading...", modifier = Modifier.padding(top = 16.dp))
        }
        if (errorState != null) {
            Text("Error: $errorState", color = Color.Red, modifier = Modifier.padding(top = 16.dp))
        }
        // Handle nullability of tracksState
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tracksState ?: emptyList()) { track ->
                TrackItem(track, navController)
            }
        }
    }
}

@Composable
fun TrackItem(track: Track, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(track.albumOfTrack.coverArt.sources.firstOrNull()?.url)
                        .apply { crossfade(true) }
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .border(BorderStroke(2.dp, Color.Gray)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = track.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = track.artists.items.joinToString(", ") { it.profile.name }, fontSize = 16.sp, color = Color.Gray)
                Text(text = "ID: ${track.id}", fontSize = 14.sp, color = Color.Gray)
            }
            Button(onClick = {
                navController.navigate("trackDetails/${track.id}")
            }) {
                Text("Details")
            }
        }
    }
}
