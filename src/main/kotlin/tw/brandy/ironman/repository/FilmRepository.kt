package tw.brandy.ironman.repository

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.toOption
import arrow.core.traverse
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.pgclient.PgPool
import io.vertx.mutiny.sqlclient.Row
import io.vertx.mutiny.sqlclient.Tuple
import tw.brandy.ironman.AppError
import tw.brandy.ironman.entity.Film
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FilmRepository(val client: PgPool) {
    suspend fun findByEpisodeId(id: Int): Either<AppError, Film> = Either.catch {
        client.preparedQuery("SELECT episodeId,title,director,releaseDate,updater FROM films WHERE episodeId = $1")
            .execute(Tuple.of(id)).awaitSuspending().firstOrNull()
    }.mapLeft { AppError.DatabaseProblem(it) }
        .flatMap { it.toOption().toEither(ifEmpty = { AppError.NoThisFilm(id) }) }
        .flatMap(::from)
    suspend fun findAll(): Either<AppError, List<Film>> = Either.catch {
        client.preparedQuery("SELECT episodeId,title,director,releaseDate,updater FROM films")
            .execute().awaitSuspending()
    }.mapLeft { e -> AppError.DatabaseProblem(e) }
        .flatMap { list -> list.traverse(::from) }
    suspend fun add(film: Film): Either<AppError, Film> = Either.catch {
        client.preparedQuery(
            " INSERT INTO films (episodeId,title,director,releaseDate,updater) " +
                " VALUES ($1,$2,$3,$4,$5)"
        ).execute(toFilmTuple(film)).awaitSuspending()
    }.mapLeft { AppError.DatabaseProblem(it) }.map { film }
    suspend fun update(film: Film): Either<AppError, Int> = Either.catch {
        client.preparedQuery(
            "UPDATE films SET title=$2,director=$3,releaseDate=$4,updater=$5 WHERE episodeId = $1"
        ).execute(toFilmTuple(film)).awaitSuspending()
    }.mapLeft { AppError.DatabaseProblem(it) }.map { it.rowCount() }
    private fun toFilmTuple(film: Film): Tuple =
        Tuple.wrap(listOf(film.episodeID, film.title, film.director, film.releaseDate, film.updater))
    suspend fun delete(film: Film): Either<AppError, Int> = Either.catch {
        client.preparedQuery("DELETE FROM films WHERE episodeId = $1")
            .execute(Tuple.of(film.episodeID)).awaitSuspending()
    }.mapLeft { AppError.DatabaseProblem(it) }.map { it.rowCount() }

    suspend fun count(): Either<AppError, Long> = Either.catch {
        client.preparedQuery("SELECT count(*) cnt FROM films")
            .execute().awaitSuspending()
    }.mapLeft { AppError.DatabaseProblem(it) }.map { it.first().getLong("cnt") }

    private fun from(row: Row) = Either.catch {
        Film(
            row.getString("title"),
            row.getLong("episodeid").toInt(),
            row.getString("director"),
            row.getLocalDate("releasedate"),
            row.getString("updater")
        )
    }.mapLeft { AppError.DatabaseProblem(it) }
}
