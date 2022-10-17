package tw.brandy.ironman.resource

import arrow.core.Either
import arrow.core.flatMap
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.RestResponse
import tw.brandy.ironman.AppError
import tw.brandy.ironman.DatabaseProblem
import tw.brandy.ironman.JsonSerializationFail
import tw.brandy.ironman.NoThisFilm
import tw.brandy.ironman.WrongUUIDFormat
import tw.brandy.ironman.resource.ResponseHandler.toResponse

object ResponseHandler {
    private val LOG: Logger = Logger.getLogger(ResponseHandler::class.java)
    fun toResponse(err: AppError): RestResponse<String> = when (err) {
        is JsonSerializationFail -> {
            LOG.error("Json Serialization Failed", err.e)
            RestResponse.status(
                RestResponse.Status.BAD_REQUEST,
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

        is NoThisFilm -> {
            LOG.error("Film Id ${err.id} is not exist")
            RestResponse.status(RestResponse.Status.NOT_FOUND, "Film Id ${err.id} is not exist")
        }

        is WrongUUIDFormat -> RestResponse.status(RestResponse.Status.BAD_REQUEST, "Not a UUID: ${err.str} ")
    }
}

inline fun <reified T> Either<AppError, T>.toRestResponse(): RestResponse<String> =
    toRestResponseBase(this, RestResponse.Status.OK)

inline fun <reified T> Either<AppError, T>.toCreatedResponse(): RestResponse<String> =
    toRestResponseBase(this, RestResponse.Status.CREATED)

inline fun <reified T> toRestResponseBase(either: Either<AppError, T>, status: RestResponse.Status): RestResponse<String> =
    either.flatMap { obj ->
        Either.catch { Json.encodeToString(obj) }
            .mapLeft { JsonSerializationFail(it) }
    }.fold(
        ifRight = { RestResponse.ResponseBuilder.ok(it).status(status).build() },
        ifLeft = { toResponse(it) }
    )
