package tw.brandy.ironman

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test

@QuarkusTest
class FilmResourceTest {

    @Test
    fun testHelloEndpoint() {
        Given { log().all() }
        When {
            get("/hello")
        } Then {
            statusCode(200)
            body(equalTo("Hello from RESTEasy Reactive"))
        }
    }
}
