package tw.brandy.ironman.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class AddFilmForm(
    @field:JsonProperty("title")
    val title: Title,
    @field:JsonProperty("director")
    val director: Director,
    @field:JsonProperty("releaseDate")
    val releaseDate: ReleaseDate
)
