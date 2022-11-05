package tw.brandy.ironman.service

import arrow.core.Either
import com.sksamuel.aedile.core.caffeineBuilder
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.eclipse.microprofile.rest.client.inject.RestClient
import tw.brandy.ironman.FruitServiceCallError
import tw.brandy.ironman.entity.FruityVice
import javax.enterprise.context.ApplicationScoped
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@ApplicationScoped
class FruitService(@RestClient val fruityViceService: FruityViceService) {

    val cache = caffeineBuilder<String, FruityVice> {
        expireAfterWrite = 14.toDuration(DurationUnit.SECONDS)
    }.build { findByName(it) }

    private suspend fun findByName(name: String) = fruityViceService.getFruitByName(name)
        .onFailure().retry().atMost(3).awaitSuspending()

    suspend fun findByNameWithCache(name: String) = Either.catch {
        cache.get(name)
    }.mapLeft { FruitServiceCallError(it) }
}
