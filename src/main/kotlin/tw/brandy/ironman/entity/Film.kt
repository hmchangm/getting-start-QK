package tw.brandy.ironman.entity

import arrow.core.Either
import kotlinx.serialization.Serializable
import tw.brandy.ironman.AppError
import tw.brandy.ironman.DateSerializer
import tw.brandy.ironman.UuidSerializer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@JvmInline
@Serializable
value class Title(val raw: String) {
    companion object {
        val key get() = "title"
    }
}

@JvmInline
@Serializable
value class EpisodeId(@Serializable(UuidSerializer::class) private val s: UUID) {
    val raw get() = s.toString()

    companion object {
        fun from(str: String): Either<AppError.WrongUUIDFormat, EpisodeId> {
            return Either.catch {
                EpisodeId(UUID.fromString(str))
            }.mapLeft {
                AppError.WrongUUIDFormat(str)
            }
        }
        val key get() = "episodeId"
    }
}

@JvmInline
@Serializable
value class Director(val raw: String) {
    companion object {
        val key get() = "director"
    }
}

@JvmInline
@Serializable
value class ReleaseDate(@Serializable(DateSerializer::class) val raw: Date) {
    companion object {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val key get() = "releaseDay"

        fun fromIsoDate(iso: String): ReleaseDate {
            return ReleaseDate(formatter.parse(iso))
        }
    }
}

@Serializable
data class Film(
    val episodeId: EpisodeId,
    val title: Title,
    val director: Director,
    val releaseDate: ReleaseDate
)
