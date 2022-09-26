package tw.brandy.ironman.health

import io.smallrye.health.checks.UrlHealthCheck
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.health.Readiness
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.HttpMethod

@ApplicationScoped
class CutsomReadiness {

    @ConfigProperty(
        name = "com.redhat.developers.FruityViceService/mp-rest/url",
        defaultValue = "https://www.fruityvice.com"
    )
    lateinit var externalUrl: String

    @Readiness
    fun checkUrl() = UrlHealthCheck("$externalUrl/api/fruit/banana")
        .name("ExternalURL health check").requestMethod(HttpMethod.GET).statusCode(200)
}
