package tw.brandy.ironman

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import tw.brandy.ironman.test.TestUtil.getAccessToken

@QuarkusTest
class GreetingResourceTest {

    @Test
    fun testHelloEndpoint() {
        Given { auth().oauth2(getAccessToken("alice")) } When {
            get("/hello")
        } Then {
            statusCode(200)
            body(equalTo("Hello from RESTEasy Reactive back quarkus"))
        }
    }
}
