package tw.brandy.ironman.resource

import arrow.core.traverse
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import tw.brandy.ironman.service.FruitService
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/fruits")
class FruitResource(val fruitService: FruitService) {

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun fruit(@PathParam("name") name: String) = fruitService.findByNameWithCache(name).toRestResponse()

    @GET
    @Path("/par")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun allParFruit() = listOf("apple", "banana", "guava", "Apricot", "Blueberry")
        .parTraverseEither(Dispatchers.IO) {
            fruitService.findByNameWithCache(it)
        }.toRestResponse()

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun allFruit() = listOf("apple", "banana", "guava", "Apricot", "Blueberry")
        .traverse {
            fruitService.findByNameWithCache(it)
        }.toRestResponse()
}
