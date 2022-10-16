package tw.brandy.ironman

import arrow.core.traverse
import io.quarkus.runtime.StartupEvent
import kotlinx.coroutines.runBlocking
import tw.brandy.ironman.entity.Director
import tw.brandy.ironman.entity.EpisodeId
import tw.brandy.ironman.entity.Film
import tw.brandy.ironman.entity.ReleaseDate
import tw.brandy.ironman.entity.Title
import tw.brandy.ironman.repository.FilmRepository
import java.time.Instant
import java.util.Date
import java.util.UUID
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class Startup(val filmRepository: FilmRepository) {

    fun onStart(@Observes ev: StartupEvent) {
        runBlocking {
            listOf(
                Film(
                    EpisodeId(UUID.randomUUID()),
                    Title("A New Hope"),
                    Director("George Lucas"),
                    ReleaseDate.fromIsoDate("1977-05-25")
                ),
                Film(
                    EpisodeId(UUID.randomUUID()),
                    Title("The Empire Strikes Back"),
                    Director("George Lucas"),
                    ReleaseDate.fromIsoDate("1980-05-25")
                ),
                Film(
                    EpisodeId(UUID.randomUUID()),
                    Title("Return Of The Jedi"),
                    Director("George Lucas"),
                    ReleaseDate.fromIsoDate("1983-05-25")
                )

            ).traverse { filmRepository.add(it) }
        }
    }
}
