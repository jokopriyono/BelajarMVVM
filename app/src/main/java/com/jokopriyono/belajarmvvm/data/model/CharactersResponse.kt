package com.jokopriyono.belajarmvvm.data.model


import com.google.gson.annotations.SerializedName

data class CharactersResponse(
    @SerializedName("data")
    val characterData: List<CharacterData>
)

data class CharacterData(
    @SerializedName("films")
    val films: List<String>,
    @SerializedName("_id")
    val id: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("parkAttractions")
    val parkAttractions: List<String>,
    @SerializedName("shortFilms")
    val shortFilms: List<String>,
    @SerializedName("tvShows")
    val tvShows: List<String>,
    @SerializedName("url")
    val url: String,
    @SerializedName("videoGames")
    val videoGames: List<String>
)