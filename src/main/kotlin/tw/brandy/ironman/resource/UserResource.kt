package tw.brandy.ironman.resource

import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.resteasy.reactive.NoCache
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/api/users")
class UsersResource {
    @Inject
    lateinit var idToken: JsonWebToken

    @GET
    @Path("/me")
    @NoCache
    @RolesAllowed("user")
    fun me() = idToken

    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    fun admin() = "granted"
}
