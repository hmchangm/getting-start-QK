package tw.brandy.ironman.test

import arrow.core.getOrElse
import arrow.core.traverse
import io.quarkus.mongodb.panache.kotlin.reactive.runtime.KotlinReactiveMongoOperations
import io.quarkus.runtime.StartupEvent
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.smallrye.mutiny.infrastructure.Infrastructure
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import tw.brandy.ironman.entity.Film
import tw.brandy.ironman.entity.FilmEntity
import tw.brandy.ironman.repository.FilmRepo
import tw.brandy.ironman.repository.FilmRepository
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class Startup(val filmRepository: FilmRepository) {

    fun onStart(@Observes ev: StartupEvent?) {
        GlobalScope.async(Infrastructure.getDefaultExecutor().asCoroutineDispatcher()) {
            KotlinReactiveMongoOperations.INSTANCE.deleteAll(FilmEntity::class.java).awaitSuspending()
            listOf(
                Film(
                    episodeId = 4,
                    title = "A New Hope",
                    director = "George Lucas",
                    releaseDate = LocalDate.parse("1977-05-25"),
                    updater = "brandy"
                )
            ).plus(
                Film(
                    episodeId = 5,
                    title = "The Empire Strikes Back",
                    director = "George Lucas",
                    releaseDate = LocalDate.parse("1980-05-21"),
                    updater = "louis"
                )
            ).plus(
                Film(
                    episodeId = 6,
                    title = "Return Of The Jedi",
                    director = "George Lucas",
                    releaseDate = LocalDate.parse("1983-05-21"),
                    updater = "brandy"
                )
            ).let { list -> list.traverse { FilmRepo.add(it) } }
            println("Data inserts ${FilmRepo.findAll().getOrElse { emptyList() }}")
        }
    }
}
