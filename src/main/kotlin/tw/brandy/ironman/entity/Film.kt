package tw.brandy.ironman.entity

import arrow.core.Either
import tw.brandy.ironman.WrongUUIDFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@JvmInline
value class Title(val raw: String) {
    companion object {
        val key get() = "title"
    }
}

@JvmInline
value class EpisodeId(private val s: UUID) {
    val raw get() = s.toString()

    companion object {
        fun from(str: String): Either<WrongUUIDFormat, EpisodeId> {
            return Either.catch {
                EpisodeId(UUID.fromString(str))
            }.mapLeft {
                WrongUUIDFormat(str)
            }
        }
        val key get() = "episodeId"
    }
}

@JvmInline
value class Director(val raw: String) {
    companion object {
        val key get() = "director"
    }
}

@JvmInline
value class ReleaseDate(val raw: Date) {
    companion object {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val key get() = "releaseDay"

        fun fromIsoDate(iso: String): ReleaseDate {
            return ReleaseDate(formatter.parse(iso))
        }
    }
}

data class Film(
    val episodeId: EpisodeId,
    val title: Title,
    val director: Director,
    val releaseDate: ReleaseDate
)
