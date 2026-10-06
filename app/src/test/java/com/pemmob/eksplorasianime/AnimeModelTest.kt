package com.pemmob.eksplorasianime

import com.pemmob.eksplorasianime.data.model.Aired
import com.pemmob.eksplorasianime.data.model.AiredProp
import com.pemmob.eksplorasianime.data.model.Anime
import com.pemmob.eksplorasianime.data.model.DateProp
import com.pemmob.eksplorasianime.data.model.Genre
import com.pemmob.eksplorasianime.data.model.Studio
import org.junit.Assert.assertEquals
import org.junit.Test

class AnimeModelTest {

    @Test
    fun `test displayTitle with null safety fallback`() {
        val animeWithBothTitles = Anime(
            malId = 1,
            title = "Cowboy Bebop",
            titleEnglish = "Cowboy Bebop English"
        )
        assertEquals("Cowboy Bebop", animeWithBothTitles.displayTitle)

        val animeWithEnglishOnly = Anime(
            malId = 2,
            title = null,
            titleEnglish = "Trigun"
        )
        assertEquals("Trigun", animeWithEnglishOnly.displayTitle)

        val animeWithoutTitles = Anime(
            malId = 3,
            title = null,
            titleEnglish = null
        )
        assertEquals("Judul Tidak Diketahui", animeWithoutTitles.displayTitle)
    }

    @Test
    fun `test displayScore formatting`() {
        val animeWithScore = Anime(malId = 1, score = 8.75)
        assertEquals("8.75", animeWithScore.displayScore)

        val animeWithoutScore = Anime(malId = 2, score = null)
        assertEquals("N/A", animeWithoutScore.displayScore)
    }

    @Test
    fun `test displayYear fallback to aired prop`() {
        val animeWithDirectYear = Anime(malId = 1, year = 1998)
        assertEquals("1998", animeWithDirectYear.displayYear)

        val animeWithAiredYear = Anime(
            malId = 2,
            year = null,
            aired = Aired(
                prop = AiredProp(
                    from = DateProp(year = 2002)
                )
            )
        )
        assertEquals("2002", animeWithAiredYear.displayYear)

        val animeWithoutYear = Anime(malId = 3, year = null, aired = null)
        assertEquals("N/A", animeWithoutYear.displayYear)
    }

    @Test
    fun `test collection operations for genres and studios`() {
        val anime = Anime(
            malId = 1,
            genres = listOf(
                Genre(malId = 1, name = "Action"),
                Genre(malId = 2, name = "Sci-Fi")
            ),
            studios = listOf(
                Studio(malId = 14, name = "Sunrise")
            )
        )

        assertEquals("Action, Sci-Fi", anime.displayGenres)
        assertEquals("Sunrise", anime.displayStudios)
    }
}
