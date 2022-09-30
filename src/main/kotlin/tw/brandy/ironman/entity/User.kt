package tw.brandy.ironman.entity

import org.eclipse.microprofile.jwt.JsonWebToken

class User internal constructor(idToken: JsonWebToken) {
    val userName: String
    init {
        userName = idToken.name
    }
}