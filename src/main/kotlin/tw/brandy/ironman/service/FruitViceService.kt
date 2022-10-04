package tw.brandy.ironman.service

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import tw.brandy.ironman.entity.FruityVice
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType


@Path("/api/fruit")
@RegisterRestClient
interface FruityViceService {
    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getFruitByName(@PathParam("name") name: String?): FruityVice?
}
