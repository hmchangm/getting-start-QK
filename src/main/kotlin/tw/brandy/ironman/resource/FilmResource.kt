package tw.brandy.ironman.resource

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import org.jboss.resteasy.reactive.RestResponse
import tw.brandy.ironman.AppError
import tw.brandy.ironman.entity.Film
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
class FilmResource(val filmService: FilmService, val mapper: ObjectMapper) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun list(): RestResponse<String> = filmService.getAllFilms()
        .fold(ifLeft = { AppError.toResponse(it) }, ifRight = (toJsonResponse))

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun getById(id: Int) = filmService.getFilm(id)
        .fold(ifLeft = { AppError.toResponse(it) }, ifRight = (toJsonResponse))

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun add(film: Film) = filmService.save(film)
        .toRestResponse()

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun update(film: Film) = filmService.update(film)
        .toRestResponse()

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun delete(id: Int) = filmService.delete(id)
        .toRestResponse()

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun count() = filmService.getFilmCount()
        .toRestResponse()

    val toJsonResponse: (Any) -> RestResponse<String> = { obj ->
        mapper.writeValueAsString(obj).let { RestResponse.ok(it) }
    }

    fun Either<AppError, Any>.toRestResponse(): RestResponse<String> =
        this.fold(
            ifLeft = { AppError.toResponse(it) },
            ifRight = (toJsonResponse)
        )
}
