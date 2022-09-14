package tw.brandy.ironman

import io.quarkus.runtime.StartupEvent
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.smallrye.mutiny.infrastructure.Infrastructure
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class Startup(val filmRepository: FilmRepository) {

    fun onStart(@Observes ev: StartupEvent?) {
        GlobalScope.async(Infrastructure.getDefaultExecutor().asCoroutineDispatcher()) {
            filmRepository.deleteAll().awaitSuspending()
            listOf(
                FilmEntity(
                    episodeId = 4,
                    title = "A New Hope",
                    director = "George Lucas",
                    releaseDate = LocalDate.parse("1977-05-25").toJavaLocalDate()
                )
            ).plus(
                FilmEntity(
                    episodeId = 5,
                    title = "The Empire Strikes Back",
                    director = "George Lucas",
                    releaseDate = LocalDate.parse("1980-05-21").toJavaLocalDate()
                )
            ).plus(
                FilmEntity(
                    episodeId = 6,
                    title = "Return Of The Jedi",
                    director = "George Lucas",
                    releaseDate = LocalDate.parse("1983-05-21").toJavaLocalDate()
                )
            ).let { filmRepository.persist(it) }.awaitSuspending()
        }
    }
}
