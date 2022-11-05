package tw.brandy.ironman

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.test.junit.QuarkusTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import tw.brandy.ironman.entity.AddFilmForm
import tw.brandy.ironman.entity.Director
import tw.brandy.ironman.entity.Film
import tw.brandy.ironman.entity.ReleaseDate
import tw.brandy.ironman.entity.Title
import javax.inject.Inject

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

    @Inject
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `test add update remove`() {
        val id = Given {
            contentType("application/json")
            body(
                AddFilmForm(
                    Title("Spider Man"),
                    Director("Sam Raimi"),
                    ReleaseDate.fromIsoDate("2002-04-29")
                ).let { objectMapper.writeValueAsString(it) }
            )
        } When {
            post("/films")
        } Then {
            statusCode(201)
            body("title", `is`("Spider Man"))
            body("director", equalTo("Sam Raimi"))
        } Extract {
            body().asString().let { objectMapper.readValue(it, Film::class.java) }.episodeId
        }

        Given {
            contentType("application/json")
            body(
                Film(
                    id,
                    Title("Spider Man"),
                    Director("Nobody"),
                    ReleaseDate.fromIsoDate("2002-04-25")
                ).let { objectMapper.writeValueAsString(it) }
            )
        } When {
            put("/films/$id")
        } Then {
            statusCode(200)
            body("director", equalTo("Nobody"))
        }

        When {
            delete("/films/${id.raw}")
        } Then {
            statusCode(200)
        }
    }
}
