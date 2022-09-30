package tw.brandy.ironman

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.smallrye.jwt.build.Jwt
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import tw.brandy.ironman.test.TestUtil.getAccessToken

@QuarkusTest
class UserResourceTest {

    @Test
    fun `test api me`() {
        Given {
            auth().oauth2(getAccessToken("alice"))
        } When {
            get("/api/users/me")
        } Then {
            statusCode(200)
            body("userName", `is`("alice"))
        }
    }

}