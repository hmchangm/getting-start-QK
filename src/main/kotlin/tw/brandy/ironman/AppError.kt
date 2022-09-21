package tw.brandy.ironman

import org.jboss.logging.Logger
import javax.ws.rs.core.Response

sealed class AppError {
    data class DatabaseProblem(val e: Throwable) : AppError()
    class JsonSerializationFail(val e: Throwable) : AppError()
    data class NoThisFilm(val epId: Int) : AppError()

    companion object {
        private val LOG: Logger = Logger.getLogger(AppError::class.java)
        fun toResponse(err: AppError): Response = when (err) {
            is JsonSerializationFail -> {
                LOG.error("Json Serialization Failed", err.e)
                Response.serverError().entity("Json Serialization Failed\n ${err.e.stackTraceToString()}")
                    .build()
            }
            is DatabaseProblem -> {
                LOG.error("db error", err.e)
                Response.serverError().entity("Db Connect Error \n ${err.e.stackTraceToString()}")
                    .build()
            }

            is NoThisFilm -> Response.status(404).entity("Film Id ${err.epId} is not exist").build()
        }
    }
}
