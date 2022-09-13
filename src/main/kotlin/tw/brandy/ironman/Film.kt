package tw.brandy.ironman

import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.LocalDateComponentSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Film(
    val title: String,
    val episodeID: Int,
    val director: String,
    @Serializable(LocalDateComponentSerializer::class)
    val releaseDate: LocalDate
)
