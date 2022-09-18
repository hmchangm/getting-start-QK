package tw.brandy.ironman

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/films")
class FilmResource(val filmService: FilmService) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun list() = filmService.getAllFilms()

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun getById(id: Int) = filmService.getFilm(id)

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun add(film: Film) = filmService.save(film)

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun update(film: Film) = filmService.update(film)

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun delete(id: Int) = filmService.delete(id)

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun count(): Long = filmService.getFilmCount()
}
