package tw.brandy.ironman.resource

import io.quarkus.oidc.IdToken
import org.eclipse.microprofile.jwt.JsonWebToken
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/api/tokens")
class TokenResource {
    @Inject
    @IdToken
    lateinit var idToken: JsonWebToken

    @Inject
    lateinit var accessToken: JsonWebToken

    @Produces("text/html")
    @GET
    fun getTokens() = "<html><body><h3>idToken</h3>$idToken" +
        "<h3>accessToken</h3>$accessToken</body></html>"
}
