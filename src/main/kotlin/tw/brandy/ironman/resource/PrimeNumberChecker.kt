package tw.brandy.ironman.resource

import org.eclipse.microprofile.metrics.MetricUnits
import org.eclipse.microprofile.metrics.annotation.Counted
import org.eclipse.microprofile.metrics.annotation.Gauge
import org.eclipse.microprofile.metrics.annotation.Timed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import kotlin.math.floor
import kotlin.math.sqrt

@Path("/")
class PrimeNumberChecker {

    private var highestPrimeNumberSoFar: Long = 2

    @GET
    @Path("/{number}")
    @Produces(MediaType.TEXT_PLAIN)
    @Counted(name = "performedChecks", description = "How many primality checks have been performed.")
    @Timed(
        name = "checksTimer",
        description = "A measure of how long it takes to perform the primality test.",
        unit = MetricUnits.MILLISECONDS
    )
    fun checkIfPrime(number: Long): String {
        if (number < 1) {
            return "Only natural numbers can be prime numbers."
        }
        if (number == 1L) {
            return "1 is not prime."
        }
        if (number == 2L) {
            return "2 is prime."
        }
        if (number % 2 == 0L) {
            return "$number is not prime, it is divisible by 2."
        }
        var i = 3
        while (i < floor(sqrt(number.toDouble())) + 1) {
            if (number % i == 0L) {
                return "$number is not prime, is divisible by $i."
            }
            i += 2
        }
        if (number > highestPrimeNumberSoFar) {
            highestPrimeNumberSoFar = number
        }
        return "$number is prime."
    }

    @Gauge(name = "highestPrimeNumberSoFar", unit = MetricUnits.NONE, description = "Highest prime number so far.")
    fun highestPrimeNumberSoFar(): Long {
        return highestPrimeNumberSoFar
    }
}
