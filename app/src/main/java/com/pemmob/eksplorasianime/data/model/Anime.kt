package com.pemmob.eksplorasianime.data.model

import com.google.gson.annotations.SerializedName
import java.util.Locale

data class Anime(
    @SerializedName("mal_id")
    val malId: Int,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("title_english")
    val titleEnglish: String? = null,

    @SerializedName("title_japanese")
    val titleJapanese: String? = null,

    @SerializedName("score")
    val score: Double? = null,

    @SerializedName("rating")
    val rating: String? = null,

    @SerializedName("year")
    val year: Int? = null,

    @SerializedName("episodes")
    val episodes: Int? = null,

    @SerializedName("synopsis")
    val synopsis: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("duration")
    val duration: String? = null,

    @SerializedName("season")
    val season: String? = null,

    @SerializedName("aired")
    val aired: Aired? = null,

    @SerializedName("genres")
    val genres: List<Genre>? = null,

    @SerializedName("studios")
    val studios: List<Studio>? = null
) {
    val displayTitle: String
        get() = title ?: titleEnglish ?: "Judul Tidak Diketahui"

    val displayScore: String
        get() = score?.let { String.format(Locale.US, "%.2f", it) } ?: "N/A"

    val displayAgeRating: String
        get() = rating ?: "Tidak Ada Rating Usia"

    val displayYear: String
        get() = (year ?: aired?.prop?.from?.year)?.toString() ?: "N/A"

    val displayEpisodes: String
        get() = episodes?.let { "$it Episode" } ?: "? Episode"

    val displayGenres: String
        get() = genres?.map { it.name }?.filter { it.isNotBlank() }?.joinToString(", ")
            ?.ifEmpty { "Tidak ada genre" } ?: "Tidak ada genre"

    val displayStudios: String
        get() = studios?.map { it.name }?.filter { it.isNotBlank() }?.joinToString(", ")
            ?.ifEmpty { "Tidak diketahui" } ?: "Tidak diketahui"

    val displaySynopsis: String
        get() = synopsis?.takeIf { it.isNotBlank() } ?: "Sinopsis belum tersedia untuk anime ini."
}

data class Aired(
    @SerializedName("string")
    val string: String? = null,

    @SerializedName("prop")
    val prop: AiredProp? = null
)

data class AiredProp(
    @SerializedName("from")
    val from: DateProp? = null,

    @SerializedName("to")
    val to: DateProp? = null
)

data class DateProp(
    @SerializedName("day")
    val day: Int? = null,

    @SerializedName("month")
    val month: Int? = null,

    @SerializedName("year")
    val year: Int? = null
)

data class Genre(
    @SerializedName("mal_id")
    val malId: Int? = null,

    @SerializedName("name")
    val name: String = ""
)

data class Studio(
    @SerializedName("mal_id")
    val malId: Int? = null,

    @SerializedName("name")
    val name: String = ""
)

data class AnimeResponse(
    @SerializedName("data")
    val data: List<Anime> = emptyList()
)

data class AnimeDetailResponse(
    @SerializedName("data")
    val data: Anime
)
