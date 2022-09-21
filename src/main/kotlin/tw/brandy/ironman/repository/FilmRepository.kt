package tw.brandy.ironman.repository

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.toOption
import arrow.core.traverse
import io.quarkus.mongodb.panache.kotlin.reactive.runtime.KotlinReactiveMongoOperations.Companion.INSTANCE
import io.smallrye.mutiny.coroutines.awaitSuspending
import tw.brandy.ironman.AppError
import tw.brandy.ironman.entity.FilmEntity
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FilmRepository {
    suspend fun findByEpisodeId(id: Int): Either<AppError, FilmEntity> = Either.catch {
        INSTANCE.find(
            FilmEntity::class.java,
            "episodeId",
            id
        ).firstResult().awaitSuspending()
    }.mapLeft { AppError.DatabaseProblem(it) }
        .flatMap {
            it.toOption()
                .toEither(ifEmpty = { AppError.NoThisFilm(id) })
        }
        .flatMap(anyToEntity)
    suspend fun findAll(): Either<AppError, List<FilmEntity>> = Either.catch {
        INSTANCE.findAll(FilmEntity::class.java).list().awaitSuspending()
    }.mapLeft { e -> AppError.DatabaseProblem(e) }
        .flatMap { list -> list.traverse { anyToEntity(it) } }
    suspend fun persistOrUpdate(film: FilmEntity): Either<AppError, FilmEntity> = Either.catch {
        INSTANCE.persistOrUpdate(film).awaitSuspending()
    }.mapLeft { AppError.DatabaseProblem(it) }.map { film }

    suspend fun delete(film: FilmEntity): Either<AppError, FilmEntity> = Either.catch {
        INSTANCE.delete(film).awaitSuspending()
    }.mapLeft { AppError.DatabaseProblem(it) }.map { film }
    suspend fun count(): Either<AppError, Long> = Either.catch {
        INSTANCE.count(FilmEntity::class.java).awaitSuspending()
    }.mapLeft { AppError.DatabaseProblem(it) }

    val anyToEntity: (Any?) -> Either<AppError, FilmEntity> = { it ->
        Either.catch {
            it as FilmEntity
        }.mapLeft { AppError.CastToFilmFail(it) }
    }
}
