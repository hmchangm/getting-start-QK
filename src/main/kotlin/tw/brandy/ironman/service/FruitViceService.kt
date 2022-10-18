package tw.brandy.ironman.service

import io.quarkus.cache.CacheResult
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import tw.brandy.ironman.entity.FruityVice
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/api/fruit")
@RegisterRestClient(configKey = "fruits-api")
interface FruityViceService {
    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @CacheResult(cacheName = "find-fruit-by-name")
    fun getFruitByName(@PathParam("name") name: String): Uni<FruityVice>
}