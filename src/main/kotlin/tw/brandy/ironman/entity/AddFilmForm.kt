package tw.brandy.ironman.entity

import kotlinx.serialization.Serializable

@Serializable
data class AddFilmForm(
    val title: Title,
    val director: Director,
    val releaseDate: ReleaseDate
)
