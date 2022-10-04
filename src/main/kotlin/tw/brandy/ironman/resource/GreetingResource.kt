package tw.brandy.ironman.resource

import arrow.core.Either
import arrow.core.identity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.eclipse.microprofile.config.ConfigProvider
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.resteasy.reactive.RestResponse
import tw.brandy.ironman.AppError
import tw.brandy.ironman.entity.KaqConfig
import tw.brandy.ironman.service.FruityViceService
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/")
class GreetingResource(val kaqConfig: KaqConfig, @RestClient val fruityViceService: FruityViceService) {

    @ConfigProperty(name = "greeting.message")
    lateinit var message: String

    @ConfigProperty(name = "greeting.suffix", defaultValue = "!")
    lateinit var suffix: String

    @ConfigProperty(name = "greeting.name")
    var name: String? = null

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    suspend fun hello() = "$message from RESTEasy Reactive $suffix $name"

    @GET
    @Path("/kaq")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun kaq() = mapOf(
        "dbName" to ConfigProvider.getConfig().getValue("quarkus.mongodb.database", String::class.java)
    )

    @GET
    @Path("/fruit/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun fruit(@PathParam("name") name: String) = Either.catch {
        withContext(Dispatchers.IO) {
            fruityViceService.getFruitByName(name)
        }
    }.mapLeft { AppError.DatabaseProblem(it) }.fold(
        ifRight = ::identity,
        ifLeft = { AppError.toResponse(it) }
    )
}
