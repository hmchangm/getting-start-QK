package tw.brandy.ironman.service

import arrow.core.Either
import io.quarkus.cache.CacheResult
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.eclipse.microprofile.rest.client.inject.RestClient
import tw.brandy.ironman.FruitServiceCallError
import tw.brandy.ironman.entity.FruityVice
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitService(@RestClient val fruityViceService: FruityViceService) {

    @CacheResult(cacheName = "find-fruit-by-name")
    fun findByNameCall(name: String): Uni<FruityVice> =
        fruityViceService.getFruitByName(name)
            .onFailure().retry().atMost(3)

    suspend fun findByName(name: String) = Either.catch {
        findByNameCall(name).awaitSuspending()
    }.mapLeft { FruitServiceCallError(it) }
}
