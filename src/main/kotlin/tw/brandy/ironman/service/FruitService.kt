package tw.brandy.ironman.service

import arrow.core.Either
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.eclipse.microprofile.rest.client.inject.RestClient
import tw.brandy.ironman.FruitServiceCallError
import tw.brandy.ironman.entity.FruityVice
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitService(@RestClient val fruityViceService: FruityViceService) {

    suspend fun findByNameCall(name: String): FruityVice =
        fruityViceService.getFruitByName(name)
            .onFailure().retry().atMost(3).awaitSuspending()

    suspend fun findByName(name: String) = Either.catch {
        findByNameCall(name)
    }.mapLeft { FruitServiceCallError(it) }
}
