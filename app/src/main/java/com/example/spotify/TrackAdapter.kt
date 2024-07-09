package com.example.spotify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify.model.AlbumItem
import com.squareup.picasso.Picasso

class TrackAdapter(private val tracks: List<AlbumItem>) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val trackName: TextView = view.findViewById(R.id.track_name)
        val artistName: TextView = view.findViewById(R.id.artist_name)
        val coverArt: ImageView = view.findViewById(R.id.cover_art)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = tracks[position]
        holder.trackName.text = track.data.name
        holder.artistName.text = track.data.artists.items.joinToString(", ") { it.profile.name }
        Picasso.get().load(track.data.coverArt.sources.firstOrNull()?.url).into(holder.coverArt)
    }

    override fun getItemCount() = tracks.size
}
