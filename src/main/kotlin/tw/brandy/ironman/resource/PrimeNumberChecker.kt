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

@Path("/prime")
class PrimeNumberChecker {

    private var highestPrimeNumberSoFar: Long = 2

    @GET
    @Path("/{number}")
    @Produces(MediaType.TEXT_PLAIN)
    @Counted(name = "performedChecks", description = "How many primality checks have been performed.")
    @Timed(name = "checksTimer", description = "timer", unit = MetricUnits.MILLISECONDS)
    fun checkIfPrime(number: Long): String {
        return when {
            number < 1 -> "Only natural numbers can be prime numbers."
            number == 1L -> "1 is not prime."
            number == 2L -> "2 is prime."
            number % 2 == 0L -> "$number is not prime, it is divisible by 2."
            else -> {
                var i = 3
                val end = floor(sqrt(number.toDouble())) + 1
                while (i < end) {
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
        }
    }

    @Gauge(name = "highestPrimeNumberSoFar", unit = MetricUnits.NONE, description = "Highest prime number so far.")
    fun highestPrimeNumberSoFar(): Long {
        return highestPrimeNumberSoFar
    }
}
