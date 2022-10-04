package tw.brandy.ironman.resource

import arrow.core.Either
import arrow.core.flatMap
import com.fasterxml.jackson.databind.ObjectMapper
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.resteasy.reactive.RestResponse
import tw.brandy.ironman.AppError
import tw.brandy.ironman.entity.Film
import tw.brandy.ironman.entity.UpsertFilm
import tw.brandy.ironman.service.FilmService
import tw.brandy.ironman.service.UserService
import javax.inject.Inject
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
class FilmResource(val filmService: FilmService, val mapper: ObjectMapper) {

    @Inject
    var idToken: JsonWebToken? = null

    @GET
    suspend fun list(): RestResponse<String> = filmService.getAllFilms()
        .toRestResponse()

    @GET
    @Path("/{id}")
    suspend fun getById(id: Int) = filmService.getFilm(id)
        .toRestResponse()

    @POST
    suspend fun add(film: UpsertFilm) = UserService.fromIdToken(idToken).map { user ->
        Film(film.title, film.episodeID, film.director, film.releaseDate, user.userName)
    }.flatMap { filmService.save(it) }
        .toRestResponse()

    @PUT
    @Path("/{id}")
    suspend fun update(id: Int, film: UpsertFilm) = UserService.fromIdToken(idToken).map { user ->
        Film(film.title, id, film.director, film.releaseDate, user.userName)
    }.flatMap { filmService.update(it) }
        .toRestResponse()

    @DELETE
    @Path("/{id}")
    suspend fun delete(id: Int) = filmService.delete(id)
        .toRestResponse()

    @GET
    @Path("/count")
    suspend fun count() = filmService.getFilmCount()
        .toRestResponse()

    public fun Either<AppError, Any>.toRestResponse(): RestResponse<String> =
        this.fold(
            ifRight = { obj ->
                mapper.writeValueAsString(obj).let { RestResponse.ok(it) }
            },
            ifLeft = { AppError.toResponse(it) }
        )
}
