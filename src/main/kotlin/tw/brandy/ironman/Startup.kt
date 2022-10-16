package tw.brandy.ironman

import io.quarkus.runtime.StartupEvent
import tw.brandy.ironman.entity.Film
import tw.brandy.ironman.entity.Title
import tw.brandy.ironman.repository.FilmRepository
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class Startup(val filmRepository: FilmRepository) {

    fun onStart(@Observes ev: StartupEvent?) {


    }
}
