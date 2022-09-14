package tw.brandy.ironman

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepository
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FilmRepository : ReactivePanacheMongoRepository<FilmEntity> {
    suspend fun findByEpisodeId(id: Int) = find("episodeId", id).firstResult().awaitSuspending()
    suspend fun findAllAsync() = findAll().list().awaitSuspending()
}
