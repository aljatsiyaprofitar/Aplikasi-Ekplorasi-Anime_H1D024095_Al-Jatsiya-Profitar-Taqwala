package com.pemmob.eksplorasianime.data.model

import com.google.gson.annotations.SerializedName
import java.util.Locale

/**
 * Data class merepresentasikan entitas Anime dari Tenrai API.
 * Memanfaatkan fitur Kotlin: Data Class, Null Safety, Default Arguments, dan Extension Properties.
 */
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
    /**
     * Memanfaatkan Null Safety (elvis operator ?:) untuk fallback judul anime.
     */
    val displayTitle: String
        get() = title ?: titleEnglish ?: "Judul Tidak Diketahui"

    /**
     * Memanfaatkan Null Safety untuk menampilkan rating / score.
     */
    val displayScore: String
        get() = score?.let { String.format(Locale.US, "%.2f", it) } ?: "N/A"

    /**
     * Rating batas usia penonton (contoh: PG-13, R - 17+).
     */
    val displayAgeRating: String
        get() = rating ?: "Tidak Ada Rating Usia"

    /**
     * Memanfaatkan Null Safety bertingkat untuk tahun rilis (year fallback ke aired.prop.from.year).
     */
    val displayYear: String
        get() = (year ?: aired?.prop?.from?.year)?.toString() ?: "N/A"

    /**
     * Memanfaatkan Null Safety untuk jumlah episode.
     */
    val displayEpisodes: String
        get() = episodes?.let { "$it Episode" } ?: "? Episode"

    /**
     * Memanfaatkan Collection function (.map & .joinToString) untuk genre.
     */
    val displayGenres: String
        get() = genres?.map { it.name }?.filter { it.isNotBlank() }?.joinToString(", ")
            ?.ifEmpty { "Tidak ada genre" } ?: "Tidak ada genre"

    /**
     * Memanfaatkan Collection function (.map & .joinToString) untuk studio pembuat anime.
     */
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

/**
 * Response pembungkus untuk list anime dari Tenrai API (/v1/anime).
 */
data class AnimeResponse(
    @SerializedName("data")
    val data: List<Anime> = emptyList()
)

/**
 * Response pembungkus untuk single anime detail dari Tenrai API (/v1/anime/{id}).
 */
data class AnimeDetailResponse(
    @SerializedName("data")
    val data: Anime
)
