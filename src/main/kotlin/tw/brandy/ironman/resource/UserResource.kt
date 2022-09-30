package tw.brandy.ironman.resource

import io.quarkus.oidc.IdToken
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.resteasy.reactive.NoCache
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/api/users")
class UsersResource {

    @Inject
    lateinit var idToken: JsonWebToken

    @GET
    @Path("/me")
    @NoCache
    fun me(): User {
        return User(idToken)
    }
}

class User internal constructor(idToken: JsonWebToken) {
    val userName: String
    init {
        userName = idToken.name
    }
}
