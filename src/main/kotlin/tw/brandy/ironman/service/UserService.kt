package tw.brandy.ironman.service

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.toOption
import org.eclipse.microprofile.jwt.JsonWebToken
import tw.brandy.ironman.AppError
import tw.brandy.ironman.entity.User

object UserService {

    fun fromIdToken(idToken: JsonWebToken?) = idToken.toOption().toEither { AppError.idTokenEmpty() }.flatMap {
        Either.catch {
            User(it)
        }.mapLeft { AppError.CreateUserFail(it) }
    }
}
