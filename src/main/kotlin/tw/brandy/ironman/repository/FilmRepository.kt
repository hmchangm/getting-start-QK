package tw.brandy.ironman.repository

import arrow.core.*
import arrow.core.continuations.either
import com.mongodb.client.model.Filters
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.quarkus.mongodb.reactive.ReactiveMongoCollection
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.bson.Document
import tw.brandy.ironman.AppError
import tw.brandy.ironman.DatabaseProblem
import tw.brandy.ironman.NoThisFilm
import tw.brandy.ironman.entity.*
import java.lang.RuntimeException
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FilmRepository(val mongoClient: ReactiveMongoClient) {

    val fruitCollection: ReactiveMongoCollection<Document> by lazy {
        mongoClient.getDatabase("ironman").getCollection("fruit")
    }
    suspend fun findByEpisodeId(episodeId: EpisodeId): Either<AppError, Film> = Either.catch {
        fruitCollection.find(Filters.eq(EpisodeId.key, episodeId.raw)).toUni().awaitSuspending()
    }.mapLeft { e -> DatabaseProblem(e) }
        .flatMap { it.toOption().toEither(ifEmpty = { NoThisFilm(episodeId) }) }
        .flatMap { from(it) }

    suspend fun findAll(): Either<AppError, List<Film>> = Either.catch {
        fruitCollection.find().collect().asList().awaitSuspending()
    }.mapLeft { e -> DatabaseProblem(e) }
        .flatMap { list -> list.traverse { from(it) } }
    suspend fun add(film: Film): Either<AppError, Film> = Either.catch {
        to(film).let { fruitCollection.insertOne(it).awaitSuspending() }
    }.mapLeft { DatabaseProblem(it) }.flatMap {
        when (it.insertedId.toOption()) {
            is Some -> film.right()
            is None -> DatabaseProblem(RuntimeException("Not Inserted")).left()
        }
    }

    suspend fun update(film: Film): Either<AppError, Film> = Either.catch {
        to(film).let {
            fruitCollection.replaceOne(Filters.eq(EpisodeId.key, film.episodeId.raw), it)
                .awaitSuspending()
        }
    }.mapLeft { DatabaseProblem(it) }.flatMap {
        when (it.modifiedCount) {
            0L -> DatabaseProblem(RuntimeException("Not Updated")).left()
            else -> film.right()
        }
    }
    suspend fun delete(film: Film): Either<AppError, Film> = Either.catch {
        fruitCollection.deleteOne(Filters.eq(EpisodeId.key, film.episodeId.raw)).awaitSuspending()
    }.mapLeft { DatabaseProblem(it) }.flatMap {
        when (it.deletedCount) {
            0L -> NoThisFilm(film.episodeId).left()
            else -> film.right()
        }
    }

    suspend fun count(): Either<AppError, Long> = Either.catch {
        fruitCollection.countDocuments().awaitSuspending()
    }.mapLeft { DatabaseProblem(it) }

    val from: suspend (Document) -> Either<AppError, Film> = { doc ->
        either {
            Film(
                doc.getString(EpisodeId.key).let { EpisodeId.from(it) }.bind(),
                Title(doc.getString(Title.key)),
                Director(doc.getString(Director.key)),
                ReleaseDate(doc.getDate(ReleaseDate.key))
            )
        }
    }

    val to: suspend (Film) -> Document = { film ->
        Document().apply {
            append(EpisodeId.key, film.episodeId.raw)
            append(Title.key, film.title.raw)
            append(Director.key, film.director.raw)
            append(ReleaseDate.key, film.releaseDate.raw)
        }
    }
}
