package tw.brandy.ironman

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test
import tw.brandy.ironman.entity.FilmWithActor
import java.util.UUID
import javax.inject.Inject

@QuarkusTest
class FilmDataTest {

    @Inject
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `test json`() {
        val str = """{"episodeId":"${UUID.randomUUID()}",
             "title":"ABCD","director":"Brandy",
             "actors":["Alison","Sylvia"]
             }
        """.trimMargin()
        println(objectMapper.readValue(str, FilmWithActor::class.java))
    }
}
