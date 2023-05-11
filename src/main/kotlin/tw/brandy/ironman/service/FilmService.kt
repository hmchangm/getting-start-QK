package tw.brandy.ironman.service

import arrow.core.flatMap
import tw.brandy.ironman.entity.Film
import tw.brandy.ironman.entity.FilmEntity
import tw.brandy.ironman.repository.FilmRepo
import tw.brandy.ironman.repository.FilmRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FilmService(val filmRepository: FilmRepository) {

    suspend fun getAllFilms() = FilmRepo.findAll()

    suspend fun getFilmCount() = FilmRepo.findAll().map { it.size }

    suspend fun getFilm(id: Int) = FilmRepo.findByEpisodeId(id)
    suspend fun save(film: Film) = FilmRepo.add(film)
    suspend fun update(film: Film) = FilmRepo.update(film)
    suspend fun delete(id: Int) =  FilmRepo.findByEpisodeId(id)
        .flatMap { FilmRepo.delete(it) }

}
