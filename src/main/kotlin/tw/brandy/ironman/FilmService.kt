package tw.brandy.ironman
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FilmService(val filmRepository: FilmRepository) {

    suspend fun getAllFilms() = filmRepository.findAllAsync().map(entityToModel)

    suspend fun getFilmCount() = filmRepository.count().awaitSuspending()

    suspend fun getFilm(id: Int) = filmRepository.findByEpisodeId(id)?.let(entityToModel)
    suspend fun save(film: Film) = film.let(modelToEntity)
        .let(filmRepository::persist)
        .awaitSuspending().let(entityToModel)
    suspend fun update(film: Film) = filmRepository.findByEpisodeId(film.episodeID)
        ?.let { film.let(modelToEntity).copy(id = it.id) }
        ?.let { filmRepository.update(it) }?.awaitSuspending()?.let(entityToModel)
    suspend fun delete(id: Int) = filmRepository.findByEpisodeId(id)
        ?.let { filmRepository.delete(it) }?.awaitSuspending()

    private val modelToEntity: (Film) -> FilmEntity = { film ->
        FilmEntity(
            title = film.title,
            episodeId = film.episodeID,
            director = film.director,
            releaseDate = film.releaseDate.toJavaLocalDate()
        )
    }

    private val entityToModel: (FilmEntity) -> Film = {
        Film(it.title, it.episodeId, it.director, it.releaseDate.toKotlinLocalDate())
    }
}
