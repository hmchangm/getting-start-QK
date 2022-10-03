package tw.brandy.ironman.service

import arrow.core.flatMap
import tw.brandy.ironman.entity.Film
import tw.brandy.ironman.repository.FilmRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FilmService(val filmRepository: FilmRepository) {

    suspend fun getAllFilms() = filmRepository.findAll()

    suspend fun getFilmCount() = filmRepository.count()

    suspend fun getFilm(id: Int) = filmRepository.findByEpisodeId(id)
    suspend fun save(film: Film) = filmRepository.add(film)
        .flatMap { filmRepository.findByEpisodeId(film.episodeID) }

    suspend fun delete(id: Int) = filmRepository.findByEpisodeId(id)
        .flatMap { filmRepository.delete(it) }

    suspend fun update(film: Film) = filmRepository.update(film)
        .flatMap { filmRepository.findByEpisodeId(film.episodeID) }
}
