package tw.brandy.ironman

import io.quarkus.test.junit.QuarkusTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
class FilmResourceTest {

    @Test
    fun `test list all`() {
        Given {
            contentType(ContentType.JSON)
        } When {
            get("/films")
        } Then {
            statusCode(200)
            body("title", `is`(listOf("A New Hope", "The Empire Strikes Back", "Return Of The Jedi")))
        }
    }

    @Test
    fun `test add than update than delete`() {
        Given {
            contentType("application/json")
            body(
                Film("Spider Man", 100, "Sam Raimi", LocalDate.parse("2002-04-29"))
                    .let { Json.encodeToString(it) }
            )
        } When {
            post("/films")
        } Then {
            statusCode(200)
            body("title", `is`("Spider Man"))
            body("director", equalTo("Sam Raimi"))
        }

        Given {
            contentType("application/json")
            body(
                Film("Spider Man", 100, "Nobody", LocalDate.parse("2002-04-29"))
                    .let { Json.encodeToString(it) }
            )
        } When {
            put("/films/100")
        } Then {
            statusCode(200)
            body("director", equalTo("Nobody"))
        }

        When {
            delete("/films/100")
        } Then {
            statusCode(204)
        }
    }
}
