package tw.brandy.ironman.service

import arrow.core.Either
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.eclipse.microprofile.rest.client.inject.RestClient
import tw.brandy.ironman.AppError
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitService(@RestClient val fruityViceService: FruityViceService) {

    suspend fun findByName(name: String) = Either.catch {
        fruityViceService.getFruitByName(name)
            .onFailure().retry().atMost(3).awaitSuspending()
    }.mapLeft {
        when {
            it.message.orEmpty().contains("status code 404") -> AppError.NoThisFruit(name)
            else -> AppError.FruitServiceCallError(it)
        }
    }
}
