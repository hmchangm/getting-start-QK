package tw.brandy.ironman.service

import arrow.core.Either
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.eclipse.microprofile.rest.client.inject.RestClient
import tw.brandy.ironman.FruitServiceCallError
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitService(@RestClient val fruityViceService: FruityViceService) {

    suspend fun findByName(name: String) = Either.catch {
        fruityViceService.getFruitByName(name)
            .onFailure().retry().atMost(3).awaitSuspending()
    }.mapLeft { FruitServiceCallError(it) }
}
