package tw.brandy.ironman.resource

import arrow.core.identity
import tw.brandy.ironman.AppError
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
    suspend fun fruit(@PathParam("name") name: String) = fruitService.findByName(name).fold(
        ifRight = ::identity,
        ifLeft = { AppError.toResponse(it) }
    )
}
