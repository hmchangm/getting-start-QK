package tw.brandy.ironman

import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.RestResponse

sealed class AppError {
    data class DatabaseProblem(val e: Throwable) : AppError()
    class JsonSerializationFail(val e: Throwable) : AppError()
    data class NoThisFilm(val epId: Int) : AppError()
    data class CastToFilmFail(val obj: Any) : AppError()

    companion object {
        private val LOG: Logger = Logger.getLogger(AppError::class.java)
        fun toResponse(err: AppError): RestResponse<String> = when (err) {
            is JsonSerializationFail -> {
                LOG.error("Json Serialization Failed", err.e)
                RestResponse.status(
                    RestResponse.Status.INTERNAL_SERVER_ERROR,
                    "Json Serialization Failed ${err.e.stackTraceToString()}"
                )
            }
            is DatabaseProblem -> {
                LOG.error("db error", err.e)
                RestResponse.status(
                    RestResponse.Status.INTERNAL_SERVER_ERROR,
                    "Db Connect Error \n ${err.e.stackTraceToString()}"
                )
            }

            is NoThisFilm -> RestResponse.status(RestResponse.Status.NOT_FOUND, "Film Id ${err.epId} is not exist")

            is CastToFilmFail -> RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR, "Any ${err.obj} is not a FilmEntity")
        }
    }
}
