package com.example.spotify

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class SongPlayActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val previewUrl = intent.getStringExtra("PREVIEW_URL")
        setContent {
            previewUrl?.let {
                PlayScreen(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    @Composable
    fun PlayScreen(previewUrl: String) {
        var isPlaying by remember { mutableStateOf(false) }
        val context = LocalContext.current

        fun playPreview(url: String) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                prepare()
                start()
            }
            isPlaying = true
            Toast.makeText(context, "Playing", Toast.LENGTH_SHORT).show()
        }

        fun pausePreview() {
            mediaPlayer?.pause()
            isPlaying = false
            Toast.makeText(context, "Paused", Toast.LENGTH_SHORT).show()
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (isPlaying) {
                        pausePreview()
                    } else {
                        playPreview(previewUrl)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isPlaying) "Pause" else "Play")
            }
        }
    }
}
