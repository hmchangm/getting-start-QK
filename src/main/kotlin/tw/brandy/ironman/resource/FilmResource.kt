package tw.brandy.ironman.resource

import arrow.core.Either
import arrow.core.flatMap
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jboss.resteasy.reactive.RestResponse
import tw.brandy.ironman.AppError
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
        .toRestResponse(RestResponse.Status.OK)

    @GET
    @Path("/{id}")
    suspend fun getById(id: String) = EpisodeId.from(id).flatMap {
        filmService.getFilm(it)
    }.toRestResponse(RestResponse.Status.OK)

    @POST
    suspend fun add(form: AddFilmForm) = filmService.add(form)
        .toRestResponse(RestResponse.Status.CREATED)

    @PUT
    @Path("/{id}")
    suspend fun update(film: Film) = filmService.update(film)
        .toRestResponse(RestResponse.Status.OK)

    @DELETE
    @Path("/{id}")
    suspend fun delete(id: String) = EpisodeId.from(id).flatMap {
        filmService.delete(it)
    }.toRestResponse(RestResponse.Status.OK)

    @GET
    @Path("/count")
    suspend fun count() = filmService.getFilmCount()
        .toRestResponse(RestResponse.Status.OK)
}
inline fun <reified T : Any> Either<AppError, T>.toRestResponse(status: RestResponse.Status): RestResponse<String> =
    this.flatMap { obj ->
        Either.catch { Json.encodeToString(obj) }
            .mapLeft { AppError.JsonSerializationFail(it) }
    }.fold(
        ifRight = { RestResponse.ResponseBuilder.ok(it).status(status).build() },
        ifLeft = { AppError.toResponse(it) }
    )
