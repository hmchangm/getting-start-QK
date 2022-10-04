package tw.brandy.ironman.service

import arrow.core.Either
import arrow.core.toOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.eclipse.microprofile.rest.client.inject.RestClient
import tw.brandy.ironman.AppError
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitService(@RestClient val fruityViceService: FruityViceService) {

    suspend fun findByName(name: String) = Either.catch {
        withContext(Dispatchers.IO) {
            fruityViceService.getFruitByName(name)
        }
    }.mapLeft {
        when {
            it.message.orEmpty().contains("status code 404") -> AppError.NoThisFruit(name)
            else -> AppError.FruitServiceCallError(it)
        }
    }
}
