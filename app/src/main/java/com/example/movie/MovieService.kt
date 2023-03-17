package com.example.movie

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("/")
    fun getListMovies(
        @Query("apikey") key: String = "b9bd48a6",
        @Query("type") type: String = "movie",
        @Query("s") title: String,
        @Query("page") page: Int
    ): Call<MovieResponse>
}