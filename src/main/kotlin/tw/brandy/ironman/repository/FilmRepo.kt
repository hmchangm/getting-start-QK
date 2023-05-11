package tw.brandy.ironman.repository

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.toOption
import org.eclipse.microprofile.config.ConfigProvider
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import tw.brandy.ironman.AppError
import tw.brandy.ironman.entity.Film
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
object FilmRepo {

    private val connectionString :String by lazy { ConfigProvider.getConfig().getValue(
            "quarkus.mongodb.connection-string",
            String::class.java
        ) }

    private val dbName :String by lazy { ConfigProvider.getConfig().getValue(
        "quarkus.mongodb.database",
        String::class.java
    ) }

    private val client = KMongo.createClient(connectionString).coroutine
    private val database = client.getDatabase(dbName) //normal java driver usage
    private val col = database.getCollection<Film>() //KMongo extension method

    suspend fun findByEpisodeId(id: Int): Either<AppError, Film> = Either.catch {
        col.findOne(Film::episodeId eq id)
    }.mapLeft { AppError.DatabaseProblem(it) }
        .flatMap { it.toOption().toEither(ifEmpty = { AppError.NoThisFilm(id) }) }
    suspend fun findAll(): Either<AppError.DatabaseProblem, List<Film>> = Either.catch {
        col.find().toList()
    }.mapLeft { e -> AppError.DatabaseProblem(e) }

    suspend fun add(film: Film): Either<AppError.DatabaseProblem, Film> = Either.catch {
        col.insertOne(film)
    }.mapLeft { AppError.DatabaseProblem(it) }.map { film }

    suspend fun update(film: Film): Either<AppError.DatabaseProblem, Film> = Either.catch {
        col.updateOne(Film::episodeId eq film.episodeId,film)
    }.mapLeft { AppError.DatabaseProblem(it) }.map { film }

    suspend fun delete(film: Film): Either<AppError.DatabaseProblem, Film> = Either.catch {
        col.deleteOne(Film::episodeId eq film.episodeId)
    }.mapLeft { AppError.DatabaseProblem(it) }.map { film }

}
