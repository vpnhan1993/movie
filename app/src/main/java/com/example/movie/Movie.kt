package com.example.movie


import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("imdbID")
    var imdbID: String? = null,
    @SerializedName("Poster")
    var poster: String? = null,
    @SerializedName("Title")
    var title: String? = null,
    @SerializedName("Type")
    var type: String? = null,
    @SerializedName("Year")
    var year: String? = null
)