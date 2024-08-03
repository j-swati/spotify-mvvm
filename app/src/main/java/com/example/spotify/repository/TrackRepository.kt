package com.example.spotify.repository

import com.example.spotify.api.ApiInterface
import com.example.spotify.model.Track
import com.example.spotify.model.TrackDetails
import com.example.spotify.model.TrackDetailsResponse
import com.example.spotify.model.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackRepository(private val apiService: ApiInterface) {
    fun searchTracks(query: String, callback: (List<Track>?) -> Unit) {
        apiService.search(query).enqueue(object : Callback<TracksResponse> {
            override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.tracks?.items?.map { it.data })
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun getTrackDetails(trackId: String, callback: (TrackDetails?) -> Unit) {
        apiService.getTracks(trackId).enqueue(object : Callback<TrackDetailsResponse> {
            override fun onResponse(call: Call<TrackDetailsResponse>, response: Response<TrackDetailsResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.tracks?.firstOrNull())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<TrackDetailsResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
}
