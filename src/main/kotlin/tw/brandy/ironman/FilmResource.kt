package tw.brandy.ironman

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/films")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FilmResource(val filmService: FilmService) {

    @GET
    suspend fun getAllFilms() = filmService.getAllFilms()

    @GET
    @Path("/count")
    suspend fun count() = filmService.getFilmCount()

    @GET
    @Path("/{id}")
    suspend fun getFilmById(id: Int) = filmService.getFilm(id)

    @POST
    suspend fun addFilm(film: Film) = filmService.save(film)
}
