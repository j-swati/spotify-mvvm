package com.example.spotify.model


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {
    @Headers(
        "x-rapidapi-key:410665d3a7mshe50edbda597d4b9p18e119jsn03f20295d999",
        "x-rapidapi-host:spotify23.p.rapidapi.com"
    )
    @GET("search")
    fun search(
        @Query("q") query: String,
        @Query("type") type: String = "multi",
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10
    ): Call<TracksResponse>

    @Headers(
        "x-rapidapi-key:410665d3a7mshe50edbda597d4b9p18e119jsn03f20295d999",
        "x-rapidapi-host:spotify23.p.rapidapi.com"
    )
    @GET("tracks")
    fun getTracks(
        @Query("ids") ids: String
    ): Call<TrackDetailsResponse>
}