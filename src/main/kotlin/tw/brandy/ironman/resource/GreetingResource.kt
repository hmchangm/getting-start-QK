package tw.brandy.ironman.resource

import org.eclipse.microprofile.config.ConfigProvider
import org.eclipse.microprofile.config.inject.ConfigProperty
import tw.brandy.ironman.entity.KaqConfig
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/")
class GreetingResource(val kaqConfig: KaqConfig) {

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
}
