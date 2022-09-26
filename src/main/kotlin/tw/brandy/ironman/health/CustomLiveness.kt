package tw.brandy.ironman.health

import org.eclipse.microprofile.health.HealthCheck
import org.eclipse.microprofile.health.HealthCheckResponse
import org.eclipse.microprofile.health.Liveness

@Liveness
class CustomLiveness : HealthCheck {

    override fun call(): HealthCheckResponse =
        HealthCheckResponse.up("I'm alive")
}