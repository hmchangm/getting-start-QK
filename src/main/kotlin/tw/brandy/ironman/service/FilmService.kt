package tw.brandy.ironman.service

import arrow.core.flatMap
import tw.brandy.ironman.entity.Film
import tw.brandy.ironman.entity.FilmEntity
import tw.brandy.ironman.repository.FilmRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FilmService(val filmRepository: FilmRepository) {

    suspend fun getAllFilms() = filmRepository.findAll().map { all -> all.map(entityToModel) }

    suspend fun getFilmCount() = filmRepository.count()

    suspend fun getFilm(id: Int) = filmRepository.findByEpisodeId(id).map(entityToModel)
    suspend fun save(film: Film) = film.let(modelToEntity)
        .let { filmRepository.persistOrUpdate(it) }
        .map(entityToModel)
    suspend fun update(film: Film) = filmRepository.findByEpisodeId(film.episodeID)
        .map { film.let(modelToEntity).copy(id = it.id) }
        .flatMap { filmRepository.persistOrUpdate(it) }
        .map(entityToModel)
    suspend fun delete(id: Int) = filmRepository.findByEpisodeId(id)
        .flatMap { filmRepository.delete(it) }

    private val modelToEntity: (Film) -> FilmEntity = { film ->
        FilmEntity(
            title = film.title,
            episodeId = film.episodeID,
            director = film.director,
            releaseDate = film.releaseDate,
            updater = film.updater
        )
    }

    private val entityToModel: (FilmEntity) -> Film = {
        Film(it.title, it.episodeId, it.director, it.releaseDate, it.updater)
    }
}
