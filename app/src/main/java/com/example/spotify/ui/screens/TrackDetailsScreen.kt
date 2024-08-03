package com.example.spotify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.spotify.R
import com.example.spotify.viewmodel.TrackViewModel
@Composable
fun TrackDetailsScreen(trackId: String?, viewModel: TrackViewModel) {
    val context = LocalContext.current
    val trackDetails by viewModel.trackDetails.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initializePlayer(context)
    }

    LaunchedEffect(trackId) {
        trackId?.let { id ->
            viewModel.getTrackDetails(id)
        }
    }

    trackDetails?.let { track ->
        Column(modifier = Modifier.padding(16.dp)) {
            val imageUrl = track.album.images.firstOrNull()?.url ?: ""
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(imageUrl)
                        .apply { crossfade(true) }
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop
            )
            Text(text = track.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = track.artists.joinToString(", ") { it.name }, fontSize = 18.sp, color = Color.Gray)
            Text(text = "Album: ${track.album.name}", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            IconButton(onClick = { viewModel.togglePlayPause() }) {
                Icon(
                    painter = painterResource(id = if (isPlaying) R.drawable.pause else R.drawable.play_arrow),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color.Black
                )
            }
        }
    } ?: run {
        Text(text = "Loading...", modifier = Modifier.padding(16.dp))
    }
}
