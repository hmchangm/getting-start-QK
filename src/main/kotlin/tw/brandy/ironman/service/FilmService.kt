package tw.brandy.ironman.service

import arrow.core.flatMap
import tw.brandy.ironman.entity.EpisodeId
import tw.brandy.ironman.entity.Film
import tw.brandy.ironman.repository.FilmRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FilmService(val filmRepository: FilmRepository) {

    suspend fun getAllFilms() = filmRepository.findAll()

    suspend fun getFilmCount() = filmRepository.count()

    suspend fun getFilm(episodeId: EpisodeId) = filmRepository.findByEpisodeId(episodeId)
    suspend fun save(film: Film) = filmRepository.add(film)
    suspend fun update(film: Film) = filmRepository.update(film)
    suspend fun delete(episodeId: EpisodeId) = getFilm(episodeId).flatMap { filmRepository.delete(it) }
}
