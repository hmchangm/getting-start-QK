package tw.brandy.ironman.test

import io.smallrye.jwt.build.Jwt

object TestUtil {

    fun getAccessToken(userName: String): String {
        return Jwt.preferredUserName(userName)
            .issuer("https://server.example.com")
            .audience("https://service.example.com")
            .sign()
    }
}
