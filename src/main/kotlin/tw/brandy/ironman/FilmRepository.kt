package tw.brandy.ironman

import io.quarkus.mongodb.panache.kotlin.PanacheMongoRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FilmRepository : PanacheMongoRepository<FilmEntity> {
    suspend fun findByName(title: String) = find("title", title).firstResult()
    suspend fun findByDirector(director: String) = find("director", director).firstResult()
}
