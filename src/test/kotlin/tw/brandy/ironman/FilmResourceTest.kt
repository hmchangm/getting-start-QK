package tw.brandy.ironman

import io.quarkus.test.junit.QuarkusTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import tw.brandy.ironman.entity.Film
import java.time.LocalDate

@QuarkusTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation::class)
@Tag("film")
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
    @Order(1)
    fun `test add`() {
        Given {
            contentType("application/json")
            body(
                """{"title":"Spider Man","episodeID":100,"director":"Sam Raimi","releaseDate":"2002-04-29"}"""
            )
        } When {
            post("/films")
        } Then {
            statusCode(200)
            body("title", `is`("Spider Man"))
            body("director", equalTo("Sam Raimi"))
        }
    }

    @Test
    @Order(2)
    fun `test update after add`() {
        Given {
            contentType("application/json")
            body(
                Film("Spider Man", 100, "Nobody", LocalDate.parse("2002-04-29"))
            )
        } When {
            put("/films/100")
        } Then {
            statusCode(200)
            body("director", equalTo("Nobody"))
        }
    }

    @Test
    @Order(3)
    fun `test delete`() {
        When {
            delete("/films/100")
        } Then {
            statusCode(200)
        }
    }
}