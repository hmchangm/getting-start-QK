package tw.brandy.ironman
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FilmService(val filmRepository: FilmRepository) {

    private val films: MutableList<Film> = ArrayList<Film>()

    init {
        films.apply {
            add(
                Film(
                    episodeID = 4,
                    title = "A New Hope",
                    director = "George Lucas",
                    releaseDate = LocalDate.parse("1977-05-25")
                )
            )

            Film(
                episodeID = 5,
                title = "The Empire Strikes Back",
                director = "George Lucas",
                releaseDate = LocalDate.parse("1980-05-21")
            ).let { add(it) }

            Film(
                episodeID = 5,
                title = "Return Of The Jedi",
                director = "George Lucas",
                releaseDate = LocalDate.parse("1983-05-25")
            ).let(::add)
        }
    }

    fun getAllFilms() = films.toList()

    fun getFilmCount() = filmRepository.count()

    fun getFilm(id: Int) = films.first { id == it.episodeID }
    fun save(film: Film) = FilmEntity().apply {
        title = film.title
        episodeId = film.episodeID
        director = film.director
        releaseDate = film.releaseDate.toJavaLocalDate()
    }.let(filmRepository::persist)
}
