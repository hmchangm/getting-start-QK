package tw.brandy.ironman

import tw.brandy.ironman.entity.EpisodeId

sealed class AppError
data class DatabaseProblem(val e: Throwable) : AppError()
class JsonSerializationFail(val e: Throwable) : AppError()
data class NoThisFilm(val id: EpisodeId) : AppError()
data class WrongUUIDFormat(val str: String) : AppError()
