package tw.brandy.ironman.resource

import arrow.core.flatMap
import org.jboss.resteasy.reactive.RestResponse
import tw.brandy.ironman.entity.*
import tw.brandy.ironman.service.FilmService
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/films")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FilmResource(val filmService: FilmService) {

    @GET
    suspend fun list(): RestResponse<String> = filmService.getAllFilms()
        .toRestResponse()

    @GET
    @Path("/{id}")
    suspend fun getById(id: String) = EpisodeId.from(id).flatMap {
        filmService.getFilm(it)
    }.toRestResponse()

    @POST
    suspend fun add(form: AddFilmForm) = filmService.add(form)
        .toCreatedResponse()

    @PUT
    @Path("/{id}")
    suspend fun update(film: Film) = filmService.update(film)
        .toRestResponse()

    @DELETE
    @Path("/{id}")
    suspend fun delete(id: String) = EpisodeId.from(id).flatMap {
        filmService.delete(it)
    }.toRestResponse()

    @GET
    @Path("/count")
    suspend fun count() = filmService.getFilmCount()
        .toRestResponse()
}
