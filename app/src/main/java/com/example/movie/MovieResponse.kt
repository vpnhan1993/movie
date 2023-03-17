package com.example.movie


import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("Response")
    var response: String? = null,
    @SerializedName("Search")
    var results: List<Movie>? = null,
    @SerializedName("totalResults")
    var totalResults: String? = null
)